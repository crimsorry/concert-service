package hhplus.tdd.concert.application.service.concert;

import hhplus.tdd.concert.application.dto.*;
import hhplus.tdd.concert.application.service.ConcertService;
import hhplus.tdd.concert.domain.entity.concert.SeatStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDateTime;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class ConcertServiceUnitTest {

    @InjectMocks
    private ConcertService concertService;

    @Test
    public void 예약_가능_날짜_조회() {
        // given
        String queueToken = "testToken";

        // when
        List<ConcertScheduleDto> result = concertService.loadConcertDate(queueToken);

        // then
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).scheduleId());
        assertEquals(LocalDateTime.of(2024, 10, 15, 12, 1, 1), result.get(0).openDate());
    }

    @Test
    public void 예약_가능_좌석_조회() {
        // given
        String queueToken = "testToken";
        long scheduleId = 1L;

        // when
        List<ConcertSeatDto> result = concertService.loadConcertSeat(queueToken, scheduleId);

        // then
        assertEquals(1, result.size());
        assertEquals("A01", result.get(0).seatNum());
        assertEquals(3000, result.get(0).amount());
        assertEquals(SeatStatus.STAND_BY, result.get(0).seatStatus());
    }

    @Test
    public void 좌석_예약_성공() {
        // given
        String queueToken = "testToken";
        long seatId = 1L;

        // when
        PayDto result = concertService.processReserve(queueToken, seatId);

        // then
        assertEquals(1L, result.reserveId());
        assertEquals(400, result.amount());
        assertEquals(false, result.isPay());
    }

}