package hhplus.tdd.concert.application.service.concert;

import hhplus.tdd.concert.application.dto.concert.ConcertScheduleDto;
import hhplus.tdd.concert.application.dto.concert.ConcertSeatDto;
import hhplus.tdd.concert.application.dto.concert.ReservationDto;
import hhplus.tdd.concert.application.dto.concert.SReserveStatus;
import hhplus.tdd.concert.application.dto.payment.PayDto;
import hhplus.tdd.concert.application.service.ConcertService;
import hhplus.tdd.concert.domain.entity.concert.*;
import hhplus.tdd.concert.domain.entity.member.Member;
import hhplus.tdd.concert.domain.entity.payment.AmountHistory;
import hhplus.tdd.concert.domain.entity.payment.Payment;
import hhplus.tdd.concert.domain.entity.waiting.Waiting;
import hhplus.tdd.concert.domain.entity.waiting.WaitingStatus;
import hhplus.tdd.concert.domain.repository.concert.ConcertScheduleRepository;
import hhplus.tdd.concert.domain.repository.concert.ConcertSeatRepository;
import hhplus.tdd.concert.domain.repository.concert.ReservationRepository;
import hhplus.tdd.concert.domain.repository.payment.PaymentRepository;
import hhplus.tdd.concert.domain.repository.waiting.WaitingRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ConcertServiceUnitTest {

    @InjectMocks
    private ConcertService concertService;

    @Mock
    private WaitingRepository waitingRepository;

    @Mock
    private ConcertScheduleRepository concertScheduleRepository;

    @Mock
    private ConcertSeatRepository concertSeatRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private PaymentRepository paymentRepository;

    // given
    private final String waitingToken = "testToken";
    private final String title = "드라큘라";
    private final LocalDateTime now = LocalDateTime.now();
    private final Member member = new Member(1L, "김소리", 0);
    private final Waiting waiting = new Waiting(1L, member, waitingToken, WaitingStatus.STAND_BY, LocalDateTime.now(), LocalDateTime.now().plusMinutes(30));
    private final Concert concert = new Concert(1L, title, "부산문화회관 대극장");
    private final ConcertSchedule concertSchedule = new ConcertSchedule(1L, concert, now, LocalDateTime.now().minusDays(1), LocalDateTime.now().plusDays(1), 50);
    private final ConcertSeat concertSeat = new ConcertSeat(1L, concertSchedule, "A01", 140000, SeatStatus.STAND_BY);
    private final List<ConcertSchedule> concertSchedules = List.of(concertSchedule);
    private final List<ConcertSeat> concertSeats = List.of(concertSeat);

    @Test
    public void 예약_가능_날짜_조회() {
        // when
        when(waitingRepository.findByToken(waitingToken)).thenReturn(waiting);
        when(concertScheduleRepository.findByConcertScheduleDates(any(LocalDateTime.class), any(Integer.class)))
                .thenReturn(concertSchedules);

        // then
        List<ConcertScheduleDto> result = concertService.loadConcertDate(waitingToken);

        // 결과 검증
        assertEquals(1, result.size());
        assertEquals(title, result.get(0).concertTitle());
    }

    @Test
    public void 예약_가능_좌석_조회() {
        // when
        when(waitingRepository.findByToken(waitingToken)).thenReturn(waiting);
        when(concertScheduleRepository.findByScheduleId(concertSchedule.getScheduleId())).thenReturn(concertSchedule);
        when(concertSeatRepository.findBySchedule(concertSchedule)).thenReturn(concertSeats);

        // then
        List<ConcertSeatDto> result = concertService.loadConcertSeat(waitingToken, concertSchedule.getScheduleId());

        // 결과 검증
        assertEquals(1, result.size());
        assertEquals("A01", result.get(0).seatNum());
    }

    @Test
    public void 좌석_예약() {
        // when
        when(concertSeatRepository.findBySeatId(concertSeat.getSeatId())).thenReturn(concertSeat);
        when(waitingRepository.findByToken(waitingToken)).thenReturn(waiting);
        when(reservationRepository.save(any(Reservation.class))).thenAnswer(invocation -> {
            Reservation reservation = invocation.getArgument(0);
            reservation.setReserveId(1L);
            reservation.setReserveStatus(ReserveStatus.RESERVED);
            return reservation;
        });
        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> {
            Payment payment = invocation.getArgument(0);
            payment.setPayId(1L);
            payment.setIsPay(true);
            return payment;
        });

        // then
        PayDto result = concertService.processReserve(waitingToken, 1L);

        // 결과검증
        assertEquals(true, result.isPay());
    }

}