package hhplus.tdd.concert.app.application.payment.service;

import hhplus.tdd.concert.app.application.payment.dto.LoadAmountQuery;
import hhplus.tdd.concert.app.application.payment.dto.UpdateChargeCommand;
import hhplus.tdd.concert.app.application.reservation.dto.ReservationCommand;
import hhplus.tdd.concert.app.domain.concert.entity.ConcertSeat;
import hhplus.tdd.concert.app.domain.exception.ErrorCode;
import hhplus.tdd.concert.app.domain.payment.entity.AmountHistory;
import hhplus.tdd.concert.app.domain.payment.entity.Payment;
import hhplus.tdd.concert.app.domain.payment.repository.AmountHistoryRepository;
import hhplus.tdd.concert.app.domain.payment.repository.PaymentRepository;
import hhplus.tdd.concert.app.domain.reservation.entity.Reservation;
import hhplus.tdd.concert.app.domain.waiting.entity.Member;
import hhplus.tdd.concert.app.domain.waiting.entity.ActiveToken;
import hhplus.tdd.concert.app.domain.waiting.repository.MemberRepository;
import hhplus.tdd.concert.app.domain.waiting.repository.WaitingRepository;
import hhplus.tdd.concert.config.exception.FailException;
import hhplus.tdd.concert.config.types.PointType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.logging.LogLevel;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PayService {

    private final AmountHistoryRepository amountHistoryRepository;
    private final PaymentRepository paymentRepository;
    private final WaitingRepository waitingRepository;
    private final MemberRepository memberRepository;

    /* 잔액 충전 */
    @Transactional
    public UpdateChargeCommand chargeAmount(String waitingToken, int amount){
        ActiveToken activeToken = waitingRepository.findByTokenOrThrow(waitingToken)
                .orElseThrow(() -> new FailException(ErrorCode.NOT_FOUND_WAITING_MEMBER, LogLevel.ERROR));

        long memberId = activeToken.getMemberId();
        Member member = memberRepository.findByMemberIdWithPessimisticLock(memberId);

        AmountHistory.checkAmountMinusOrZero(amount);
        Member.checkMemberCharge(member, amount);

        member.charge(amount);
        AmountHistory amountHistory = AmountHistory.generateAmountHistory(amount, PointType.CHARGE, member);
        amountHistoryRepository.save(amountHistory);

        return new UpdateChargeCommand(true);
    }

    @Transactional
    public UpdateChargeCommand chargeAmountOptimisticLock(String waitingToken, int amount){
        ActiveToken activeToken = waitingRepository.findByTokenOrThrow(waitingToken)
                .orElseThrow(() -> new FailException(ErrorCode.NOT_FOUND_WAITING_MEMBER, LogLevel.ERROR));

        long memberId = activeToken.getMemberId();
        Member member = memberRepository.findByMemberIdWithOptimisticLock(memberId);

        log.error("memberid: {}", member.getMemberId());

        // 예외처리
        AmountHistory.checkAmountMinusOrZero(amount);
        Member.checkMemberCharge(member, amount);

        // 포인트 충전
        member.charge(amount);
        AmountHistory amountHistory = AmountHistory.generateAmountHistory(amount, PointType.CHARGE, member);
        amountHistoryRepository.save(amountHistory);

        return new UpdateChargeCommand(true);
    }

    @Transactional
    @Retryable(
            retryFor = {ObjectOptimisticLockingFailureException.class},
            maxAttempts = 20,
            backoff = @Backoff(100) // delay 0.1초
    )
    public UpdateChargeCommand chargeAmountOptimisticLockRetry(String waitingToken, int amount){
        ActiveToken activeToken = waitingRepository.findByTokenOrThrow(waitingToken)
                .orElseThrow(() -> new FailException(ErrorCode.NOT_FOUND_WAITING_MEMBER, LogLevel.ERROR));

        long memberId = activeToken.getMemberId();
        Member member = memberRepository.findByMemberId(memberId);

        AmountHistory.checkAmountMinusOrZero(amount);
        Member.checkMemberCharge(member, amount);

        member.charge(amount);

        AmountHistory amountHistory = AmountHistory.generateAmountHistory(amount, PointType.CHARGE, member);
        amountHistoryRepository.save(amountHistory);

        return new UpdateChargeCommand(true);
    }

    /* 잔액 조회 */
    public LoadAmountQuery loadAmount(String waitingToken){
        ActiveToken activeToken = waitingRepository.findByTokenOrThrow(waitingToken)
                .orElseThrow(() -> new FailException(ErrorCode.NOT_FOUND_WAITING_MEMBER, LogLevel.ERROR));

        long memberId = activeToken.getMemberId();
        Member member = memberRepository.findByMemberId(memberId);

        // 잔액 조회
        return new LoadAmountQuery(member.getCharge());
    }

    /* 결제 처리 */
    @Transactional
    public ReservationCommand processPay(String waitingToken, long payId){
        ActiveToken activeToken = waitingRepository.findByTokenOrThrow(waitingToken)
                .orElseThrow(() -> new FailException(ErrorCode.NOT_FOUND_WAITING_MEMBER, LogLevel.ERROR));
        long memberId = activeToken.getMemberId();
        Member member = memberRepository.findByMemberId(memberId);

        // 결제 정보
        Payment payment = paymentRepository.findByPayIdWithPessimisticLock(payId);
        Payment.checkPaymentExistence(payment);
        Payment.checkPaymentStatue(payment);

        ConcertSeat concertSeat = payment.getReservation().getSeat();
        ConcertSeat.checkConcertSeatReserved(concertSeat);
        Member.checkMemberChargeLess(member, payment.getAmount());
        Reservation reservation = payment.getReservation();

        // 결제 완료 처리
        payment.done();
        concertSeat.close();
        reservation.complete();
        member.withdraw(payment.getAmount());
        waitingRepository.deleteActiveToken("waitingToken", activeToken.getToken() + ":" + activeToken.getMemberId() + ":" + activeToken.getExpiredAt());
        AmountHistory amountHistory = AmountHistory.generateAmountHistory(payment.getAmount(), PointType.USE, member);
        amountHistoryRepository.save(amountHistory);
        return ReservationCommand.from(reservation);
    }

    @Transactional
    public ReservationCommand processPayOptimisticLock(String waitingToken, long payId){
        ActiveToken activeToken = waitingRepository.findByTokenOrThrow(waitingToken)
                .orElseThrow(() -> new FailException(ErrorCode.NOT_FOUND_WAITING_MEMBER, LogLevel.ERROR));
        long memberId = activeToken.getMemberId();
        Member member = memberRepository.findByMemberId(memberId);

        // 결제 정보
        Payment payment = paymentRepository.findByPayIdOptimisticLock(payId);
        Payment.checkPaymentExistence(payment);
        Payment.checkPaymentStatue(payment);

        ConcertSeat concertSeat = payment.getReservation().getSeat();
        ConcertSeat.checkConcertSeatReserved(concertSeat);
        Member.checkMemberChargeLess(member, payment.getAmount());
        Reservation reservation = payment.getReservation();

        // 결제 완료 처리
        payment.done();
        concertSeat.close();
        reservation.complete();
        member.withdraw(payment.getAmount());
        AmountHistory amountHistory = AmountHistory.generateAmountHistory(payment.getAmount(), PointType.USE, member);
        amountHistoryRepository.save(amountHistory);
        waitingRepository.deleteActiveToken("waitingToken", activeToken.getToken() + ":" + activeToken.getMemberId() + ":" + activeToken.getExpiredAt());
        return ReservationCommand.from(reservation);
    }

    @Transactional
    @Retryable(
            retryFor = ObjectOptimisticLockingFailureException.class,
            maxAttempts = 20, // 최대 재시도 횟수
            backoff = @Backoff(100) // 재시작 간격
    )
    public ReservationCommand processPayOptimisticLockRetry(String waitingToken, long payId){
        ActiveToken activeToken = waitingRepository.findByTokenOrThrow(waitingToken)
                .orElseThrow(() -> new FailException(ErrorCode.NOT_FOUND_WAITING_MEMBER, LogLevel.ERROR));
        long memberId = activeToken.getMemberId();
        Member member = memberRepository.findByMemberId(memberId);

        Payment payment = paymentRepository.findByPayIdOptimisticLock(payId);
        Payment.checkPaymentExistence(payment);
        Payment.checkPaymentStatue(payment);

        ConcertSeat concertSeat = payment.getReservation().getSeat();
        ConcertSeat.checkConcertSeatReserved(concertSeat);
        Member.checkMemberChargeLess(member, payment.getAmount());
        Reservation reservation = payment.getReservation();

        payment.done();
        concertSeat.close();
        reservation.complete();
        member.withdraw(payment.getAmount());
        waitingRepository.deleteActiveToken("waitingToken", activeToken.getToken() + ":" + activeToken.getMemberId() + ":" + activeToken.getExpiredAt());
        AmountHistory amountHistory = AmountHistory.generateAmountHistory(payment.getAmount(), PointType.USE, member);
        amountHistoryRepository.save(amountHistory);
        return ReservationCommand.from(reservation);
    }

}
