package hhplus.tdd.concert.application.service;

import hhplus.tdd.concert.application.dto.concert.ConcertScheduleDto;
import hhplus.tdd.concert.application.dto.concert.ConcertSeatDto;
import hhplus.tdd.concert.application.dto.concert.SSeatStatus;
import hhplus.tdd.concert.application.dto.payment.PayDto;
import hhplus.tdd.concert.domain.entity.concert.ConcertSeat;
import hhplus.tdd.concert.domain.exception.FailException;
import hhplus.tdd.concert.domain.entity.concert.ConcertSchedule;
import hhplus.tdd.concert.domain.entity.waiting.Waiting;
import hhplus.tdd.concert.domain.repository.concert.ConcertScheduleRepository;
import hhplus.tdd.concert.domain.repository.concert.ConcertSeatRepository;
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
    private final ConcertScheduleRepository concertScheduleRepository;
    private final ConcertSeatRepository concertSeatRepository;

    // TODO: 스케줄러 - 대기열 토큰 만료
    // TODO: 스케줄러 - active 토큰 전환

    /* 예약 가능 날짜 조회 */
    public List<ConcertScheduleDto> loadConcertDate(String waitingToken){
        Waiting waiting = waitingRepository.findByToken(waitingToken);
        Waiting.checkWaitingExistence(waiting);

        LocalDateTime now = LocalDateTime.now();
        List<ConcertSchedule> concertSchedules = concertScheduleRepository.findByConcertScheduleDates(now, 0);
        return ConcertScheduleDto.from(concertSchedules);
    }

    /* 예약 가능 좌석 조회 */
    public List<ConcertSeatDto> loadConcertSeat(String waitingToken, long scheduleId){
        Waiting waiting = waitingRepository.findByToken(waitingToken);
        Waiting.checkWaitingExistence(waiting);

        // TODO: 대기열: 토큰 순번 확인 로직 추가 필요.

        ConcertSchedule concertSchedule = concertScheduleRepository.findByScheduleId(scheduleId);
        ConcertSchedule.checkConcertScheduleExistence(concertSchedule);

        List<ConcertSeat> concertSeats = concertSeatRepository.findBySchedule(concertSchedule);
        return ConcertSeatDto.from(concertSeats);
    }

    /* 좌석 예약 요청 */
    public PayDto processReserve(String waitingToken, Long seatId){
        // TODO: wating 유효성 체크 중복 관리.
        // 대기열 토큰 존재 여부 확인
        Waiting waiting = waitingRepository.findByToken(waitingToken);
        Waiting.checkWaitingExistence(waiting);


        // 토큰 만료면 error

        // stand_by면 error > 대기하고 와라..? 이상한대


        // 만약에 좌석 조회 안하고 사용자가 예약부터 바로 들어가면 어떻하지??

        // active 인지 확인
        // active 면 좌석 예약

        // TODO: 좌석 동시성 처리

        // 좌석 존재 여부 확인
        ConcertSeat concertSeat = concertSeatRepository.findBySeatId(seatId);
        ConcertSeat.checkConcertSeatExistence(concertSeat);

        // active 상태에서 30분 동안은 좌석 예약 가능하게?
        // 아니면 그냥 자동적으로 30분 동안 동작 안하지만 않으면 되게?

        // 좌석 stand by > pending
        // 결제내역 false 생성
        // 예약 pending 생성

        return new PayDto(1L, 1L, 1L, 400, false, LocalDateTime.of(2024, 10, 10, 12, 12, 1));
    }
}
