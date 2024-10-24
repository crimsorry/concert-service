package hhplus.tdd.concert.app.application.service.payment;

import hhplus.tdd.concert.app.application.dto.reservation.ReservationCommand;
import hhplus.tdd.concert.app.application.dto.payment.LoadAmountQuery;
import hhplus.tdd.concert.app.application.dto.payment.UpdateChargeCommand;
import hhplus.tdd.concert.app.domain.repository.waiting.wrapper.WaitingWrapRepository;
import hhplus.tdd.concert.app.domain.entity.concert.ConcertSeat;
import hhplus.tdd.concert.app.domain.entity.reservation.Reservation;
import hhplus.tdd.concert.app.domain.entity.member.Member;
import hhplus.tdd.concert.app.domain.entity.payment.AmountHistory;
import hhplus.tdd.concert.app.domain.entity.payment.Payment;
import hhplus.tdd.concert.app.domain.entity.waiting.Waiting;
import hhplus.tdd.concert.app.domain.repository.payment.AmountHistoryRepository;
import hhplus.tdd.concert.app.domain.repository.payment.PaymentRepository;
import hhplus.tdd.concert.common.types.PointType;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PayService {

    private final AmountHistoryRepository amountHistoryRepository;
    private final PaymentRepository paymentRepository;
    private final WaitingWrapRepository waitingWrapRepository;

    /* 잔액 충전 */
    public UpdateChargeCommand chargeAmount(String waitingToken, int amount){
        // 대기열 존재 여부 확인
        Waiting waiting = waitingWrapRepository.findByTokenOrThrow(waitingToken);
        Member member = waiting.getMember();

        // 예외처리
        AmountHistory.checkAmountMinusOrZero(amount);
        Member.checkMemberCharge(member, amount);

        // 포인트 충전
        member.charge(amount);
        AmountHistory amountHistory = AmountHistory.generateAmountHistory(amount, PointType.CHARGE, waiting.getMember());
        amountHistoryRepository.save(amountHistory);

        return new UpdateChargeCommand(true);
    }

    /* 잔액 조회 */
    public LoadAmountQuery loadAmount(String waitingToken){
        // 대기열 존재 여부 확인
        Waiting waiting = waitingWrapRepository.findByTokenOrThrow(waitingToken);
        Member member = waiting.getMember();

        // 잔액 조회
        return new LoadAmountQuery(member.getCharge());
    }

    /* 결제 처리 */
    @Transactional
    public ReservationCommand processPay(String waitingToken, long payId){
        // 대기열 존재 여부 확인
        Waiting waiting = waitingWrapRepository.findByTokenOrThrow(waitingToken);
        Waiting.checkWaitingStatusActive(waiting);
        Member member = waiting.getMember();

        // 결제 정보
        Payment payment = paymentRepository.findByPayId(payId);
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
        member.withdraw(member.getCharge());
        waiting.stop();
        AmountHistory amountHistory = AmountHistory.generateAmountHistory(payment.getAmount(), PointType.USE, waiting.getMember());
        amountHistoryRepository.save(amountHistory);

        return ReservationCommand.from(reservation);
    }

}
