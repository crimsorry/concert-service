package hhplus.tdd.concert.app.application.service.reservation;

import hhplus.tdd.concert.app.application.dto.concert.ConcertScheduleDto;
import hhplus.tdd.concert.app.application.dto.concert.ConcertSeatDto;
import hhplus.tdd.concert.app.application.dto.payment.PayDto;
import hhplus.tdd.concert.app.application.dto.reservation.ReservationDto;
import hhplus.tdd.concert.app.application.repository.WaitingWrapRepository;
import hhplus.tdd.concert.app.domain.entity.concert.ConcertSchedule;
import hhplus.tdd.concert.app.domain.entity.concert.ConcertSeat;
import hhplus.tdd.concert.app.domain.entity.concert.Reservation;
import hhplus.tdd.concert.app.domain.entity.member.Member;
import hhplus.tdd.concert.app.domain.entity.payment.Payment;
import hhplus.tdd.concert.app.domain.entity.waiting.Waiting;
import hhplus.tdd.concert.app.domain.repository.concert.ConcertScheduleRepository;
import hhplus.tdd.concert.app.domain.repository.concert.ConcertSeatRepository;
import hhplus.tdd.concert.app.domain.repository.concert.ReservationRepository;
import hhplus.tdd.concert.app.domain.repository.payment.PaymentRepository;
import hhplus.tdd.concert.common.types.SeatStatus;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class ReservationService {

    private final ConcertScheduleRepository concertScheduleRepository;
    private final ConcertSeatRepository concertSeatRepository;
    private final ReservationRepository reservationRepository;
    private final PaymentRepository paymentRepository;
    private final WaitingWrapRepository waitingWrapRepository;

    /* 좌석 예약 요청 */
    @Transactional
    public PayDto processReserve(String waitingToken, Long seatId){
        // 비관적 락
        ConcertSeat concertSeat = concertSeatRepository.findBySeatId(seatId);

        // 대기열 존재 여부 확인
        Waiting waiting = waitingWrapRepository.findByTokenOrThrow(waitingToken);
        Member member = waiting.getMember();

        // 좌석 상태 확인
        ConcertSeat.checkConcertSeatExistence(concertSeat);
        ConcertSeat.checkConcertSeatStatus(concertSeat);

        Reservation reservation = Reservation.generateReservation(member, concertSeat);
        Payment payment = Payment.generatePayment(member, reservation);

        // 좌석 임시배정
        concertSeat.setSeatStatus(SeatStatus.RESERVED);
        waiting.setExpiredAt(LocalDateTime.now().plusMinutes(10));
        reservationRepository.save(reservation);
        paymentRepository.save(payment);

        return PayDto.from(payment, reservation);
    }

    public List<ReservationDto> loadReservation(String waitingToken){
        // 대기열 존재 여부 확인
        Waiting waiting = waitingWrapRepository.findByTokenOrThrow(waitingToken);
        Member member = waiting.getMember();

        List<Reservation> reservations = reservationRepository.findByMember(member);
        return ReservationDto.from(reservations);
    }

}
