package hhplus.tdd.concert.application.service.concert;

import hhplus.tdd.concert.app.application.dto.concert.ConcertScheduleQuery;
import hhplus.tdd.concert.app.application.dto.concert.ConcertSeatQuery;
import hhplus.tdd.concert.app.application.service.concert.ConcertService;
import hhplus.tdd.concert.app.domain.repository.concert.ConcertScheduleRepository;
import hhplus.tdd.concert.app.domain.repository.concert.ConcertSeatRepository;
import hhplus.tdd.concert.app.domain.repository.concert.ReservationRepository;
import hhplus.tdd.concert.app.domain.repository.payment.PaymentRepository;
import hhplus.tdd.concert.app.domain.repository.waiting.WaitingRepository;
import hhplus.tdd.concert.application.service.TestBase;
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

    @Test
    public void 예약_가능_날짜_조회() {
        // when
        when(waitingRepository.findByToken(testBase.waitingToken)).thenReturn(testBase.waiting);
        when(concertScheduleRepository.findByConcertScheduleDatesWithStandBySeats(any(LocalDateTime.class)))
                .thenReturn(testBase.concertSchedules);

        // then
        List<ConcertScheduleQuery> result = concertService.loadConcertDate(testBase.waitingToken);

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
        List<ConcertSeatQuery> result = concertService.loadConcertSeat(testBase.waitingToken, testBase.concertSchedule.getScheduleId());

        // 결과 검증
        assertEquals(1, result.size());
        assertEquals("A01", result.get(0).seatNum());
    }

}