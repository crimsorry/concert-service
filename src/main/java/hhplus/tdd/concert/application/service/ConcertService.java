package hhplus.tdd.concert.application.service;

import hhplus.tdd.concert.application.dto.concert.ConcertScheduleDto;
import hhplus.tdd.concert.application.dto.concert.ConcertSeatDto;
import hhplus.tdd.concert.application.dto.concert.SSeatStatus;
import hhplus.tdd.concert.application.dto.payment.PayDto;
import hhplus.tdd.concert.domain.repository.waiting.WaitingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ConcertService {

    private final WaitingRepository waitingRepository;

    /* 예약 가능 날짜 조회 */
    public List<ConcertScheduleDto> loadConcertDate(String waitingToken){
        List<ConcertScheduleDto> concertScheduleDtos = new ArrayList<>();
        concertScheduleDtos.add(new ConcertScheduleDto(1L, LocalDateTime.of(2024, 10, 15, 12, 1, 1), LocalDateTime.of(2024, 10, 8, 12, 1, 1), LocalDateTime.of(2024, 10, 25, 12, 1, 1)));
        return concertScheduleDtos;
    }

    /* 예약 가능 좌석 조회 */
    public List<ConcertSeatDto> loadConcertSeat(String waitingToken, long scheduleId){
        List<ConcertSeatDto> concertSeatDtos = new ArrayList<>();
        concertSeatDtos.add(new ConcertSeatDto(1L, "A01", 3000, SSeatStatus.STAND_BY));
        return concertSeatDtos;
    }

    /* 좌석 예약 요청 */
    public PayDto processReserve(String waitingToken, Long seatId){
        return new PayDto(1L, 1L, 1L, 400, false, LocalDateTime.of(2024, 10, 10, 12, 12, 1));
    }
}
