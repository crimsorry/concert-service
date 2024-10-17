package hhplus.tdd.concert.application.service.concert;

import hhplus.tdd.concert.application.dto.concert.ConcertScheduleDto;
import hhplus.tdd.concert.application.dto.concert.ConcertSeatDto;
import hhplus.tdd.concert.application.dto.payment.PayDto;
import hhplus.tdd.concert.application.service.BaseService;
import hhplus.tdd.concert.domain.entity.concert.ConcertSchedule;
import hhplus.tdd.concert.domain.entity.concert.ConcertSeat;
import hhplus.tdd.concert.domain.entity.concert.Reservation;
import hhplus.tdd.concert.domain.entity.concert.SeatStatus;
import hhplus.tdd.concert.domain.entity.member.Member;
import hhplus.tdd.concert.domain.entity.payment.Payment;
import hhplus.tdd.concert.domain.entity.waiting.Waiting;
import hhplus.tdd.concert.domain.repository.concert.ConcertScheduleRepository;
import hhplus.tdd.concert.domain.repository.concert.ConcertSeatRepository;
import hhplus.tdd.concert.domain.repository.concert.ReservationRepository;
import hhplus.tdd.concert.domain.repository.payment.PaymentRepository;
import hhplus.tdd.concert.domain.repository.waiting.WaitingRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ConcertService extends BaseService {

    private final ConcertScheduleRepository concertScheduleRepository;
    private final ConcertSeatRepository concertSeatRepository;
    private final ReservationRepository reservationRepository;
    private final PaymentRepository paymentRepository;

    public ConcertService(WaitingRepository waitingRepository, ConcertScheduleRepository concertScheduleRepository, ConcertSeatRepository concertSeatRepository, ReservationRepository reservationRepository, PaymentRepository paymentRepository) {
        super(waitingRepository);
        this.concertScheduleRepository = concertScheduleRepository;
        this.concertSeatRepository = concertSeatRepository;
        this.reservationRepository = reservationRepository;
        this.paymentRepository = paymentRepository;
    }

    /* 예약 가능 날짜 조회 - 스케줄러를 통해 ACTIVE 상태 토큰만 들어옴 */
    public List<ConcertScheduleDto> loadConcertDate(String waitingToken){
        findAndCheckWaiting(waitingToken);
        LocalDateTime now = LocalDateTime.now();
        List<ConcertSchedule> concertSchedules = concertScheduleRepository.findByConcertScheduleDates(now, 0);
        return ConcertScheduleDto.from(concertSchedules);
    }

    /* 예약 가능 좌석 조회 */
    public List<ConcertSeatDto> loadConcertSeat(String waitingToken, long scheduleId){
        findAndCheckWaiting(waitingToken);
        ConcertSchedule concertSchedule = concertScheduleRepository.findByScheduleId(scheduleId);
        ConcertSchedule.checkConcertScheduleExistence(concertSchedule);

        List<ConcertSeat> concertSeats = concertSeatRepository.findBySchedule(concertSchedule);
        return ConcertSeatDto.from(concertSeats);
    }

    /* 좌석 예약 요청 */
    public PayDto processReserve(String waitingToken, Long seatId){
        ConcertSeat concertSeat = concertSeatRepository.findBySeatId(seatId);
        Waiting waiting = findAndCheckWaiting(waitingToken);
        Member member = waiting.getMember();

        // 좌석 상태 확인
        ConcertSeat.checkConcertSeatExistence(concertSeat);
        ConcertSeat.checkConcertSeatStatus(concertSeat);

        // 좌석 임시배정
        Reservation reservation = Reservation.generateReservation(member, concertSeat);
        Payment payment = Payment.generatePayment(member, reservation);
        concertSeat.setSeatStatus(SeatStatus.RESERVED);
        reservationRepository.save(reservation);
        paymentRepository.save(payment);

        return PayDto.from(payment, reservation);
    }
}
