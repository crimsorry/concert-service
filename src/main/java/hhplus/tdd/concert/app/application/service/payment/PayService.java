package hhplus.tdd.concert.app.application.service.payment;

import hhplus.tdd.concert.app.application.dto.concert.ReservationDto;
import hhplus.tdd.concert.app.application.dto.payment.LoadAmountDto;
import hhplus.tdd.concert.app.application.dto.payment.UpdateChargeDto;
import hhplus.tdd.concert.app.application.repository.WaitingWrapRepository;
import hhplus.tdd.concert.app.domain.entity.concert.ConcertSeat;
import hhplus.tdd.concert.app.domain.entity.concert.Reservation;
import hhplus.tdd.concert.app.domain.entity.member.Member;
import hhplus.tdd.concert.app.domain.entity.payment.AmountHistory;
import hhplus.tdd.concert.app.domain.entity.payment.Payment;
import hhplus.tdd.concert.app.domain.entity.waiting.Waiting;
import hhplus.tdd.concert.app.domain.repository.payment.AmountHistoryRepository;
import hhplus.tdd.concert.app.domain.repository.payment.PaymentRepository;
import hhplus.tdd.concert.app.domain.repository.waiting.WaitingRepository;
import hhplus.tdd.concert.common.types.PointType;
import hhplus.tdd.concert.common.types.ReserveStatus;
import hhplus.tdd.concert.common.types.SeatStatus;
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
    public UpdateChargeDto chargeAmount(String waitingToken, int amount){
        // 대기열 존재 여부 확인
        Waiting waiting = waitingWrapRepository.findByTokenOrThrow(waitingToken);
        Member member = waiting.getMember();

        AmountHistory.checkAmountMinus(amount);
        Member.checkMemberCharge(member, amount);

        // 포인트 충전
        member.setCharge(member.getCharge() + amount);
        AmountHistory amountHistory = AmountHistory.generateAmountHistory(amount, PointType.CHARGE, waiting.getMember());
        amountHistoryRepository.save(amountHistory);

        return new UpdateChargeDto(true);
    }

    /* 잔액 조회 */
    public LoadAmountDto loadAmount(String waitingToken){
        // 대기열 존재 여부 확인
        Waiting waiting = waitingWrapRepository.findByTokenOrThrow(waitingToken);
        Member member = waiting.getMember();

        // 잔액 조회
        return new LoadAmountDto(member.getCharge());
    }

    /* 결제 처리 */
    @Transactional
    public ReservationDto processPay(String waitingToken, long payId){
        // 대기열 존재 여부 확인
        Waiting waiting = waitingWrapRepository.findByTokenOrThrow(waitingToken);
        Member member = waiting.getMember();

        // 결제 정보
        Payment payment = paymentRepository.findByPayId(payId);
        ConcertSeat concertSeat = payment.getReservation().getSeat();
        Reservation reservation = payment.getReservation();

        // 예외처리
        Payment.checkPaymentExistence(payment); // 존재여부 확인
        Payment.checkPaymentStatue(payment); // 결제 안했는지 확인
        ConcertSeat.checkConcertSeatReserved(concertSeat); // 임시배정 존재 X
        Member.checkMemberChargeLess(member, payment.getAmount()); // 잔액 부족 확인

        // 결제 완료 처리
        payment.setIsPay(true);
        concertSeat.setSeatStatus(SeatStatus.ASSIGN);
        reservation.setReserveStatus(ReserveStatus.RESERVED);
        member.setCharge(member.getCharge() - payment.getAmount());
        AmountHistory amountHistory = AmountHistory.generateAmountHistory(payment.getAmount(), PointType.USE, waiting.getMember());
        amountHistoryRepository.save(amountHistory);

        return ReservationDto.from(reservation);
    }

}
