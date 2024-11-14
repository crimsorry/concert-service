package hhplus.tdd.concert.app.application.concert.service;

import hhplus.tdd.concert.app.application.concert.dto.ConcertDTO;
import hhplus.tdd.concert.app.application.concert.dto.ConcertScheduleDTO;
import hhplus.tdd.concert.app.application.concert.dto.ConcertSeatDTO;
import hhplus.tdd.concert.app.domain.concert.entity.Concert;
import hhplus.tdd.concert.app.domain.concert.entity.ConcertSchedule;
import hhplus.tdd.concert.app.domain.concert.entity.ConcertSeat;
import hhplus.tdd.concert.app.domain.concert.repository.ConcertRepository;
import hhplus.tdd.concert.app.domain.concert.repository.ConcertScheduleRepository;
import hhplus.tdd.concert.app.domain.concert.repository.ConcertSeatRepository;
import hhplus.tdd.concert.app.domain.exception.ErrorCode;
import hhplus.tdd.concert.app.domain.waiting.repository.WaitingRepository;
import hhplus.tdd.concert.config.exception.FailException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.logging.LogLevel;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConcertService {

    private final ConcertRepository concertRepository;
    private final ConcertScheduleRepository concertScheduleRepository;
    private final ConcertSeatRepository concertSeatRepository;
    private final WaitingRepository waitingRepository;

    /* 전체 콘서트 리스트 조회 */
    @Cacheable(value = "concertList", key = "'concertList'", cacheManager = "cacheManager", unless = "#result == null || #result.isEmpty()")
    public List<ConcertDTO> loadConcert() {
        List<Concert> concerts = concertRepository.findAll();
        return ConcertDTO.from(concerts);
    }

    /* 예약 가능 날짜 조회 */
    public List<ConcertScheduleDTO> loadConcertDate(String waitingToken, Long concertId){
        waitingRepository.findByTokenOrThrow(waitingToken)
                .orElseThrow(() -> new FailException(ErrorCode.NOT_FOUND_WAITING_MEMBER, LogLevel.ERROR));

//        LocalDate startDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
//        LocalDate endDate = startDate.plusDays(1);
        PageRequest pageRequest = PageRequest.of(0, 10);

        List<ConcertSchedule> concertSchedules = concertScheduleRepository.findByConcertScheduleDatesWithStandBySeats(concertId, pageRequest);
        return ConcertScheduleDTO.from(concertSchedules);
    }

    /* 예약 가능 좌석 조회 */
    public List<ConcertSeatDTO> loadConcertSeat(String waitingToken, long scheduleId){
        waitingRepository.findByTokenOrThrow(waitingToken)
                .orElseThrow(() -> new FailException(ErrorCode.NOT_FOUND_WAITING_MEMBER, LogLevel.ERROR));

        ConcertSchedule concertSchedule = concertScheduleRepository.findByScheduleId(scheduleId);
        ConcertSchedule.checkConcertScheduleExistence(concertSchedule);

        List<ConcertSeat> concertSeats = concertSeatRepository.findBySchedule(concertSchedule);
        return ConcertSeatDTO.from(concertSeats);
    }
}
