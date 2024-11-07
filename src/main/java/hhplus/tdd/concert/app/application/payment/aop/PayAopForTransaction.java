package hhplus.tdd.concert.app.application.payment.aop;

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
import org.springframework.boot.logging.LogLevel;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class PayAopForTransaction {

    private final WaitingRepository waitingRepository;
    private final MemberRepository memberRepository;
    private final AmountHistoryRepository amountHistoryRepository;
    private final PaymentRepository paymentRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public UpdateChargeCommand processChargeAmount(final String waitingToken, final int amount) throws Throwable {
        ActiveToken activeToken = waitingRepository.findByTokenOrThrow(waitingToken)
                .orElseThrow(() -> new FailException(ErrorCode.NOT_FOUND_WAITING_MEMBER, LogLevel.ERROR));
        long memberId = activeToken.getMemberId();
        Member member = memberRepository.findByMemberIdWithPessimisticLock(memberId);

        AmountHistory.checkAmountMinusOrZero(amount);
        Member.checkMemberCharge(member, amount);

        member.charge(amount);
        AmountHistory amountHistory = AmountHistory.generateAmountHistory(amount, PointType.CHARGE, member);
        amountHistoryRepository.save(amountHistory);
        return new UpdateChargeCommand(true); // 트랜잭션 내 반환값
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ReservationCommand processPay(final String waitingToken, final long payId) throws Throwable {
        // 대기열 존재 여부 확인
        ActiveToken activeToken = waitingRepository.findByTokenOrThrow(waitingToken)
                .orElseThrow(() -> new FailException(ErrorCode.NOT_FOUND_WAITING_MEMBER, LogLevel.ERROR));

        long memberId = activeToken.getMemberId();
        Member member = memberRepository.findByMemberIdWithPessimisticLock(memberId);

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
//        waiting.stop();
        AmountHistory amountHistory = AmountHistory.generateAmountHistory(payment.getAmount(), PointType.USE, member);
        amountHistoryRepository.save(amountHistory);
        return ReservationCommand.from(reservation);
    }

}