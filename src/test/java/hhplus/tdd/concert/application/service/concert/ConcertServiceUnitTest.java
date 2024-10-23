package hhplus.tdd.concert.application.service.concert;

import hhplus.tdd.concert.app.application.dto.concert.ConcertScheduleDto;
import hhplus.tdd.concert.app.application.dto.concert.ConcertSeatDto;
import hhplus.tdd.concert.app.application.dto.payment.PayDto;
import hhplus.tdd.concert.app.application.service.concert.ConcertService;
import hhplus.tdd.concert.app.domain.entity.concert.Reservation;
import hhplus.tdd.concert.app.domain.entity.payment.Payment;
import hhplus.tdd.concert.app.domain.repository.concert.ConcertScheduleRepository;
import hhplus.tdd.concert.app.domain.repository.concert.ConcertSeatRepository;
import hhplus.tdd.concert.app.domain.repository.concert.ReservationRepository;
import hhplus.tdd.concert.app.domain.repository.payment.PaymentRepository;
import hhplus.tdd.concert.app.domain.repository.waiting.WaitingRepository;
import hhplus.tdd.concert.application.service.TestBase;
import hhplus.tdd.concert.common.types.ReserveStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ConcertServiceUnitTest {

    private final TestBase testBase = new TestBase();

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

    @Test
    public void 예약_가능_날짜_조회() {
        // when
        when(waitingRepository.findByToken(testBase.waitingToken)).thenReturn(testBase.waiting);
        when(concertScheduleRepository.findByConcertScheduleDatesWithStandBySeats(any(LocalDateTime.class)))
                .thenReturn(testBase.concertSchedules);

        // then
        List<ConcertScheduleDto> result = concertService.loadConcertDate(testBase.waitingToken);

        // 결과 검증
        assertEquals(1, result.size());
        assertEquals(testBase.title, result.get(0).concertTitle());
    }

    @Test
    public void 예약_가능_좌석_조회() {
        // when
        when(waitingRepository.findByToken(testBase.waitingToken)).thenReturn(testBase.waiting);
        when(concertScheduleRepository.findByScheduleId(testBase.concertSchedule.getScheduleId())).thenReturn(testBase.concertSchedule);
        when(concertSeatRepository.findBySchedule(testBase.concertSchedule)).thenReturn(testBase.concertSeats);

        // then
        List<ConcertSeatDto> result = concertService.loadConcertSeat(testBase.waitingToken, testBase.concertSchedule.getScheduleId());

        // 결과 검증
        assertEquals(1, result.size());
        assertEquals("A01", result.get(0).seatNum());
    }

    @Test
    public void 좌석_예약() {
        // when
        when(concertSeatRepository.findBySeatId(testBase.concertSeatStandBy.getSeatId())).thenReturn(testBase.concertSeatStandBy);
        when(waitingRepository.findByToken(testBase.waitingToken)).thenReturn(testBase.waiting);
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
        PayDto result = concertService.processReserve(testBase.waitingToken, 1L);

        // 결과검증
        assertEquals(true, result.isPay());
    }

}