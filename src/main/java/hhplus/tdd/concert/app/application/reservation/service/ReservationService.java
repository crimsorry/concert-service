package hhplus.tdd.concert.app.application.reservation.service;

import hhplus.tdd.concert.app.application.payment.dto.PayCommand;
import hhplus.tdd.concert.app.application.reservation.dto.ReservationQuery;
import hhplus.tdd.concert.app.domain.concert.entity.ConcertSeat;
import hhplus.tdd.concert.app.domain.concert.repository.ConcertSeatRepository;
import hhplus.tdd.concert.app.domain.member.entity.Member;
import hhplus.tdd.concert.app.domain.payment.entity.Payment;
import hhplus.tdd.concert.app.domain.payment.repository.PaymentRepository;
import hhplus.tdd.concert.app.domain.reservation.entity.Reservation;
import hhplus.tdd.concert.app.domain.reservation.repository.ReservationRepository;
import hhplus.tdd.concert.app.domain.waiting.entity.Waiting;
import hhplus.tdd.concert.app.domain.waiting.repository.WaitingRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ConcertSeatRepository concertSeatRepository;
    private final ReservationRepository reservationRepository;
    private final PaymentRepository paymentRepository;
    private final WaitingRepository waitingRepository;

    /* 좌석 예약 요청 */
    @Transactional
    public PayCommand processReserve(String waitingToken, Long seatId){
        // 비관적 락
        ConcertSeat concertSeat = concertSeatRepository.findBySeatIdWithPessimisticLock(seatId);

        // 좌석 상태 확인
        ConcertSeat.checkConcertSeatExistence(concertSeat);
        ConcertSeat.checkConcertSeatStatus(concertSeat);

        // 대기열 존재 여부 확인
        Waiting waiting = waitingRepository.findByTokenOrThrow(waitingToken);
        Waiting.checkWaitingStatusActive(waiting);
        Member member = waiting.getMember();

        Reservation reservation = Reservation.generateReservation(member, concertSeat);
        Payment payment = Payment.generatePayment(member, reservation);

        // 좌석 임시배정
        concertSeat.pending();
        waiting.limitPayTime();
        reservationRepository.save(reservation);
        paymentRepository.save(payment);

        return PayCommand.from(payment, reservation);
    }


    @Transactional
    public PayCommand processReserveOptimisticLock(String waitingToken, Long seatId){
        ConcertSeat concertSeat = concertSeatRepository.findBySeatIdWithOptimisticLock(seatId);

        // 좌석 상태 확인
        ConcertSeat.checkConcertSeatExistence(concertSeat);
        ConcertSeat.checkConcertSeatStatus(concertSeat);

        // 대기열 존재 여부 확인
        Waiting waiting = waitingRepository.findByTokenOrThrow(waitingToken);
        Waiting.checkWaitingStatusActive(waiting);
        Member member = waiting.getMember();

        Reservation reservation = Reservation.generateReservation(member, concertSeat);
        Payment payment = Payment.generatePayment(member, reservation);

        // 좌석 임시배정
        concertSeat.pending();
        waiting.limitPayTime();
        reservationRepository.save(reservation);
        paymentRepository.save(payment);

        return PayCommand.from(payment, reservation);
    }

    @Transactional
    @Retryable(
            retryFor = ObjectOptimisticLockingFailureException.class,
            maxAttempts = 100,
            backoff = @Backoff(100)
    )
    public PayCommand processReserveOptimisticLockRetry(String waitingToken, Long seatId){
        ConcertSeat concertSeat = concertSeatRepository.findBySeatIdWithOptimisticLock(seatId);

        // 좌석 상태 확인
        ConcertSeat.checkConcertSeatExistence(concertSeat);
        ConcertSeat.checkConcertSeatStatus(concertSeat);

        // 대기열 존재 여부 확인
        Waiting waiting = waitingRepository.findByTokenOrThrow(waitingToken);
        Waiting.checkWaitingStatusActive(waiting);
        Member member = waiting.getMember();

        Reservation reservation = Reservation.generateReservation(member, concertSeat);
        Payment payment = Payment.generatePayment(member, reservation);

        // 좌석 임시배정
        concertSeat.pending();
        waiting.limitPayTime();
        reservationRepository.save(reservation);
        paymentRepository.save(payment);

        return PayCommand.from(payment, reservation);
    }

    /* 예약 조회 */
    public List<ReservationQuery> loadReservation(String waitingToken){
        // 대기열 존재 여부 확인
        Waiting waiting = waitingRepository.findByTokenOrThrow(waitingToken);
        Member member = waiting.getMember();

        List<Reservation> reservations = reservationRepository.findByMember(member);
        return ReservationQuery.from(reservations);
    }

}
