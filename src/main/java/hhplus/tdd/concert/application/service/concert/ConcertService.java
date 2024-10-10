package hhplus.tdd.concert.application.service.concert;

import hhplus.tdd.concert.application.dto.*;
import hhplus.tdd.concert.domain.entity.concert.SeatStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ConcertService {

    /* 예약 가능 날짜 조회 */
    public List<ConcertScheduleDto> loadConcertDate(String queueToken){
        List<ConcertScheduleDto> concertScheduleDtos = new ArrayList<>();
        concertScheduleDtos.add(new ConcertScheduleDto(1L, LocalDateTime.of(2024, 10, 15, 12, 1, 1), LocalDateTime.of(2024, 10, 8, 12, 1, 1), LocalDateTime.of(2024, 10, 25, 12, 1, 1)));
        return concertScheduleDtos;
    }

    /* 예약 가능 좌석 조회 */
    public List<ConcertSeatDto> loadConcertSeat(String queueToken, long scheduleId){
        List<ConcertSeatDto> concertSeatDtos = new ArrayList<>();
        concertSeatDtos.add(new ConcertSeatDto(1L, "A01", 3000, SSeatStatus.STAND_BY));
        return concertSeatDtos;
    }

    /* 좌석 예약 요청 */
    public PayDto processReserve(String queueToken, Long seatId){
        return new PayDto(1L, 1L, 1L, 400, false, LocalDateTime.of(2024, 10, 10, 12, 12, 1));
    }
}
