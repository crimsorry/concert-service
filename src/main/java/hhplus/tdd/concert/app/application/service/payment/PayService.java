package hhplus.tdd.concert.app.application.service.payment;

import hhplus.tdd.concert.app.application.dto.payment.LoadAmountQuery;
import hhplus.tdd.concert.app.application.dto.payment.UpdateChargeCommand;
import hhplus.tdd.concert.app.application.dto.reservation.ReservationCommand;
import hhplus.tdd.concert.app.domain.entity.concert.ConcertSeat;
import hhplus.tdd.concert.app.domain.entity.member.Member;
import hhplus.tdd.concert.app.domain.entity.payment.AmountHistory;
import hhplus.tdd.concert.app.domain.entity.payment.Payment;
import hhplus.tdd.concert.app.domain.entity.reservation.Reservation;
import hhplus.tdd.concert.app.domain.entity.waiting.Waiting;
import hhplus.tdd.concert.app.domain.repository.member.MemberRepository;
import hhplus.tdd.concert.app.domain.repository.payment.AmountHistoryRepository;
import hhplus.tdd.concert.app.domain.repository.payment.PaymentRepository;
import hhplus.tdd.concert.app.domain.repository.waiting.WaitingRepository;
import hhplus.tdd.concert.common.types.PointType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
        Waiting waiting = waitingRepository.findByTokenOrThrow(waitingToken);

        long memberId = waiting.getMember().getMemberId();
        Member member = memberRepository.findByMemberIdWithPessimisticLock(memberId);

        AmountHistory.checkAmountMinusOrZero(amount);
        Member.checkMemberCharge(member, amount);

        member.charge(amount);
        AmountHistory amountHistory = AmountHistory.generateAmountHistory(amount, PointType.CHARGE, waiting.getMember());
        amountHistoryRepository.save(amountHistory);

        return new UpdateChargeCommand(true);
    }

    @Transactional
    public void chargeMemberAmount(Member member, int amount){
        member.charge(amount);
        AmountHistory amountHistory = AmountHistory.generateAmountHistory(amount, PointType.CHARGE, member);
        amountHistoryRepository.save(amountHistory);
    }

    @Transactional
    public void testOptimisticLock(Long memberId, int amount){
        Member member = memberRepository.findByMemberId(memberId);

        AmountHistory.checkAmountMinusOrZero(amount);
        Member.checkMemberCharge(member, amount);

        member.charge(amount);

        AmountHistory amountHistory = AmountHistory.generateAmountHistory(amount, PointType.CHARGE, member);
        amountHistoryRepository.save(amountHistory);
    }

    /* 잔액 조회 */
    public LoadAmountQuery loadAmount(String waitingToken){
        // 대기열 존재 여부 확인
        Waiting waiting = waitingRepository.findByTokenOrThrow(waitingToken);
        Member member = waiting.getMember();

        // 잔액 조회
        return new LoadAmountQuery(member.getCharge());
    }

    /* 결제 처리 */
    @Transactional
    public ReservationCommand processPay(String waitingToken, long payId){
        // 대기열 존재 여부 확인
        Waiting waiting = waitingRepository.findByTokenOrThrow(waitingToken);
        Waiting.checkWaitingStatusActive(waiting);
        Member member = waiting.getMember();

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
        member.withdraw(payment.getAmount()); // TODO: git에 [fix] 유저 포인트 제로 > 결제 건 당 포인트 차감 에러사항 수정
        waiting.stop();
        AmountHistory amountHistory = AmountHistory.generateAmountHistory(payment.getAmount(), PointType.USE, waiting.getMember());
        amountHistoryRepository.save(amountHistory);
        return ReservationCommand.from(reservation);
    }

}
