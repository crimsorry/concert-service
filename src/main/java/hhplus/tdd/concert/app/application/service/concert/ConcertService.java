package hhplus.tdd.concert.app.application.service.concert;

import hhplus.tdd.concert.app.application.dto.concert.ConcertScheduleQuery;
import hhplus.tdd.concert.app.application.dto.concert.ConcertSeatQuery;
import hhplus.tdd.concert.app.domain.repository.waiting.wrapper.WaitingWrapRepository;
import hhplus.tdd.concert.app.domain.entity.concert.ConcertSchedule;
import hhplus.tdd.concert.app.domain.entity.concert.ConcertSeat;
import hhplus.tdd.concert.app.domain.entity.waiting.Waiting;
import hhplus.tdd.concert.app.domain.repository.concert.ConcertScheduleRepository;
import hhplus.tdd.concert.app.domain.repository.concert.ConcertSeatRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class ConcertService {

    private final ConcertScheduleRepository concertScheduleRepository;
    private final ConcertSeatRepository concertSeatRepository;
    private final WaitingWrapRepository waitingWrapRepository;

    /* 예약 가능 날짜 조회 */
    public List<ConcertScheduleQuery> loadConcertDate(String waitingToken){
        // 대기열 존재 여부 확인
        Waiting waiting = waitingWrapRepository.findByTokenOrThrow(waitingToken);
        Waiting.checkWaitingStatusActive(waiting);

        LocalDateTime now = LocalDateTime.now();
        List<ConcertSchedule> concertSchedules = concertScheduleRepository.findByConcertScheduleDatesWithStandBySeats(now);
        return ConcertScheduleQuery.from(concertSchedules);
    }

    /* 예약 가능 좌석 조회 */
    public List<ConcertSeatQuery> loadConcertSeat(String waitingToken, long scheduleId){
        // 대기열 존재 여부 확인
        Waiting waiting = waitingWrapRepository.findByTokenOrThrow(waitingToken);
        Waiting.checkWaitingStatusActive(waiting);

        ConcertSchedule concertSchedule = concertScheduleRepository.findByScheduleId(scheduleId);
        ConcertSchedule.checkConcertScheduleExistence(concertSchedule);

        List<ConcertSeat> concertSeats = concertSeatRepository.findBySchedule(concertSchedule);
        return ConcertSeatQuery.from(concertSeats);
    }
}
