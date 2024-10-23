package hhplus.tdd.concert.app.application.service.concert;

import hhplus.tdd.concert.app.application.dto.concert.ConcertScheduleDto;
import hhplus.tdd.concert.app.application.dto.concert.ConcertSeatDto;
import hhplus.tdd.concert.app.application.dto.payment.PayDto;
import hhplus.tdd.concert.app.application.repository.WaitingWrapRepository;
import hhplus.tdd.concert.app.domain.entity.concert.ConcertSchedule;
import hhplus.tdd.concert.app.domain.entity.concert.ConcertSeat;
import hhplus.tdd.concert.app.domain.entity.concert.Reservation;
import hhplus.tdd.concert.common.types.SeatStatus;
import hhplus.tdd.concert.app.domain.entity.member.Member;
import hhplus.tdd.concert.app.domain.entity.payment.Payment;
import hhplus.tdd.concert.app.domain.entity.waiting.Waiting;
import hhplus.tdd.concert.app.domain.repository.concert.ConcertScheduleRepository;
import hhplus.tdd.concert.app.domain.repository.concert.ConcertSeatRepository;
import hhplus.tdd.concert.app.domain.repository.concert.ReservationRepository;
import hhplus.tdd.concert.app.domain.repository.payment.PaymentRepository;
import jakarta.transaction.Transactional;
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

    /* 예약 가능 날짜 조회 - 스케줄러를 통해 ACTIVE 상태 토큰만 들어옴 */
    public List<ConcertScheduleDto> loadConcertDate(String waitingToken){
        // 대기열 존재 여부 확인
        waitingWrapRepository.findByTokenOrThrow(waitingToken);

        LocalDateTime now = LocalDateTime.now();
        List<ConcertSchedule> concertSchedules = concertScheduleRepository.findByConcertScheduleDatesWithStandBySeats(now);
        return ConcertScheduleDto.from(concertSchedules);
    }

    /* 예약 가능 좌석 조회 */
    public List<ConcertSeatDto> loadConcertSeat(String waitingToken, long scheduleId){
        // 대기열 존재 여부 확인
        waitingWrapRepository.findByTokenOrThrow(waitingToken);

        ConcertSchedule concertSchedule = concertScheduleRepository.findByScheduleId(scheduleId);
        ConcertSchedule.checkConcertScheduleExistence(concertSchedule);

        List<ConcertSeat> concertSeats = concertSeatRepository.findBySchedule(concertSchedule);
        return ConcertSeatDto.from(concertSeats);
    }
}
