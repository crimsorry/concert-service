package hhplus.tdd.concert.app.application.reservation.aop;

import hhplus.tdd.concert.app.application.payment.dto.PayCommand;
import hhplus.tdd.concert.app.domain.concert.entity.ConcertSeat;
import hhplus.tdd.concert.app.domain.concert.repository.ConcertSeatRepository;
import hhplus.tdd.concert.app.domain.exception.ErrorCode;
import hhplus.tdd.concert.app.domain.payment.entity.Payment;
import hhplus.tdd.concert.app.domain.payment.repository.PaymentRepository;
import hhplus.tdd.concert.app.domain.reservation.entity.Reservation;
import hhplus.tdd.concert.app.domain.reservation.repository.ReservationRepository;
import hhplus.tdd.concert.app.domain.waiting.entity.Member;
import hhplus.tdd.concert.app.domain.waiting.entity.ActiveToken;
import hhplus.tdd.concert.app.domain.waiting.repository.MemberRepository;
import hhplus.tdd.concert.app.domain.waiting.repository.WaitingRepository;
import hhplus.tdd.concert.config.exception.FailException;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.logging.LogLevel;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ReserveAopForTransaction {

    private final WaitingRepository waitingRepository;
    private final PaymentRepository paymentRepository;
    private final ConcertSeatRepository concertSeatRepository;
    private final ReservationRepository reservationRepository;
    private final MemberRepository memberRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public PayCommand processChargeAmount(final String waitingToken, final long seatId) throws Throwable {
        ConcertSeat concertSeat = concertSeatRepository.findBySeatId(seatId);

        // 좌석 상태 확인
        ConcertSeat.checkConcertSeatExistence(concertSeat);
        ConcertSeat.checkConcertSeatStatus(concertSeat);

        // 대기열 존재 여부 확인
        ActiveToken activeToken = waitingRepository.findByTokenOrThrow(waitingToken)
                .orElseThrow(() -> new FailException(ErrorCode.NOT_FOUND_WAITING_MEMBER, LogLevel.ERROR));

        long memberId = activeToken.getMemberId();
        Member member = memberRepository.findByMemberIdWithPessimisticLock(memberId);

        Reservation reservation = Reservation.generateReservation(member, concertSeat);
        Payment payment = Payment.generatePayment(member, reservation);

        // 좌석 임시배정
        concertSeat.pending();
//        waiting.limitPayTime();
        reservationRepository.save(reservation);
        paymentRepository.save(payment);

        return PayCommand.from(payment, reservation);
    }





}