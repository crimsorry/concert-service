package hhplus.tdd.concert.app.application.concert.service;

import hhplus.tdd.concert.app.application.concert.dto.ConcertQuery;
import hhplus.tdd.concert.app.application.concert.dto.ConcertScheduleQuery;
import hhplus.tdd.concert.app.application.concert.dto.ConcertSeatQuery;
import hhplus.tdd.concert.app.domain.concert.entity.Concert;
import hhplus.tdd.concert.app.domain.concert.entity.ConcertSchedule;
import hhplus.tdd.concert.app.domain.concert.entity.ConcertSeat;
import hhplus.tdd.concert.app.domain.concert.repository.ConcertRepository;
import hhplus.tdd.concert.app.domain.waiting.entity.Waiting;
import hhplus.tdd.concert.app.domain.concert.repository.ConcertScheduleRepository;
import hhplus.tdd.concert.app.domain.concert.repository.ConcertSeatRepository;
import hhplus.tdd.concert.app.domain.waiting.repository.WaitingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
    @Cacheable(value = "List<ConcertQuery>", key = "'concertList'", cacheManager = "cacheManager", unless = "#result == null || #result.isEmpty()")
    public List<ConcertQuery> loadConcert() {
        List<Concert> concerts = concertRepository.findAll();
        return ConcertQuery.from(concerts);
    }

    /* 예약 가능 날짜 조회 */
    public List<ConcertScheduleQuery> loadConcertDate(String waitingToken){
        // 대기열 존재 여부 확인
        Waiting waiting = waitingRepository.findByTokenOrThrow(waitingToken);
        Waiting.checkWaitingStatusActive(waiting);

        LocalDateTime now = LocalDateTime.now();
        List<ConcertSchedule> concertSchedules = concertScheduleRepository.findByConcertScheduleDatesWithStandBySeats(now);
        return ConcertScheduleQuery.from(concertSchedules);
    }

    /* 예약 가능 좌석 조회 */
    public List<ConcertSeatQuery> loadConcertSeat(String waitingToken, long scheduleId){
        // 대기열 존재 여부 확인
        Waiting waiting = waitingRepository.findByTokenOrThrow(waitingToken);
        Waiting.checkWaitingStatusActive(waiting);

        ConcertSchedule concertSchedule = concertScheduleRepository.findByScheduleId(scheduleId);
        ConcertSchedule.checkConcertScheduleExistence(concertSchedule);

        List<ConcertSeat> concertSeats = concertSeatRepository.findBySchedule(concertSchedule);
        return ConcertSeatQuery.from(concertSeats);
    }
}
