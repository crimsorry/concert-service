package hhplus.tdd.concert.application.service;

import hhplus.tdd.concert.application.dto.concert.ReservationDto;
import hhplus.tdd.concert.application.dto.concert.SReserveStatus;
import hhplus.tdd.concert.application.dto.payment.LoadAmountDto;
import hhplus.tdd.concert.application.dto.payment.UpdateChargeDto;
import hhplus.tdd.concert.application.dto.waiting.MemberDto;
import hhplus.tdd.concert.domain.entity.concert.ConcertSeat;
import hhplus.tdd.concert.domain.entity.concert.Reservation;
import hhplus.tdd.concert.domain.entity.concert.ReserveStatus;
import hhplus.tdd.concert.domain.entity.concert.SeatStatus;
import hhplus.tdd.concert.domain.entity.member.Member;
import hhplus.tdd.concert.domain.entity.payment.AmountHistory;
import hhplus.tdd.concert.domain.entity.payment.Payment;
import hhplus.tdd.concert.domain.entity.payment.PointType;
import hhplus.tdd.concert.domain.entity.waiting.Waiting;
import hhplus.tdd.concert.domain.repository.payment.AmountHistoryRepository;
import hhplus.tdd.concert.domain.repository.payment.PaymentRepository;
import hhplus.tdd.concert.domain.repository.waiting.WaitingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PayService {

    private final WaitingRepository waitingRepository;
    private final AmountHistoryRepository amountHistoryRepository;
    private final PaymentRepository paymentRepository;

    /* 잔액 충전 */
    public UpdateChargeDto chargeAmount(String waitingToken, int amount){
        // 대기열 존재 여부 확인
        // TODO: waiting 메소드화 or aop
        Waiting waiting = waitingRepository.findByToken(waitingToken);
        Waiting.checkWaitingExistence(waiting);
        Member member = waiting.getMember();

        // 포인트 충전
        AmountHistory.checkAmountMinus(amount);
        Member.checkMemberCharge(member, amount);
        member.setCharge(member.getCharge() + amount);
        AmountHistory amountHistory = AmountHistory.generateAmountHistory(amount, PointType.CHARGE, waiting.getMember());
        amountHistoryRepository.save(amountHistory);

        return new UpdateChargeDto(true);
    }

    /* 잔액 조회 */
    public LoadAmountDto loadAmount(String waitingToken){
        // 대기열 존재 여부 확인
        Waiting waiting = waitingRepository.findByToken(waitingToken);
        Waiting.checkWaitingExistence(waiting);
        Member member = waiting.getMember();

        // 잔액 조회
        return new LoadAmountDto(member.getCharge());
    }

    /* 결제 처리 */
    public ReservationDto processPay(String waitingToken, long payId){
        // 대기열 존재 여부 확인
        Waiting waiting = waitingRepository.findByToken(waitingToken);
        Waiting.checkWaitingExistence(waiting);
        Member member = waiting.getMember();

        // 결제 정보
        Payment payment = paymentRepository.findByPaymentId(payId);
        ConcertSeat concertSeat = payment.getReservation().getSeat();
        Reservation reservation = payment.getReservation();
        Payment.checkPaymentExistence(payment); // 존재여부 확인
        Payment.checkPaymentStatue(payment); // 결제 안했는지 확인
        ConcertSeat.checkConcertSeatReserved(concertSeat); // 임시배정 존재 X
        // TODO: 임시배정 만료는 만료 스케줄러에서 확인
        Member.checkMemberChargeLess(member, payment.getAmount()); // 잔액 부족 확인

        // 결제 완료 처리
        payment.setIsPay(true);
        concertSeat.setSeatStatus(SeatStatus.ASSIGN);
        reservation.setReserveStatus(ReserveStatus.RESERVED);
        member.setCharge(member.getCharge() - payment.getAmount());
        // TODO: 결제 로직 공통 메소드
        AmountHistory amountHistory = AmountHistory.generateAmountHistory(payment.getAmount(), PointType.USE, waiting.getMember());
        amountHistoryRepository.save(amountHistory);

        return ReservationDto.from(reservation);
    }

}
