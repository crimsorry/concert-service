package hhplus.tdd.concert.application.service.concert;

import hhplus.tdd.concert.application.dto.concert.ConcertScheduleDto;
import hhplus.tdd.concert.application.dto.concert.ConcertSeatDto;
import hhplus.tdd.concert.application.dto.payment.PayDto;
import hhplus.tdd.concert.application.service.ConcertService;
import hhplus.tdd.concert.domain.entity.concert.Concert;
import hhplus.tdd.concert.domain.entity.concert.ConcertSchedule;
import hhplus.tdd.concert.domain.entity.concert.SeatStatus;
import hhplus.tdd.concert.domain.entity.member.Member;
import hhplus.tdd.concert.domain.entity.waiting.Waiting;
import hhplus.tdd.concert.domain.entity.waiting.WaitingStatus;
import hhplus.tdd.concert.domain.repository.concert.ConcertScheduleRepository;
import hhplus.tdd.concert.domain.repository.member.MemberRepository;
import hhplus.tdd.concert.domain.repository.waiting.WaitingRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ConcertServiceUnitTest {

    @InjectMocks
    private ConcertService concertService;

    @Mock
    private WaitingRepository waitingRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private ConcertScheduleRepository concertScheduleRepository;

    @Test
    public void 예약_가능_날짜_조회() {
        // given
        String waitingToken = "testToken";
        String title = "드라큘라";
        LocalDateTime now = LocalDateTime.now();
        Member member = new Member(1L, "김소리", 0);
        Waiting waiting = new Waiting(1L, member, waitingToken, WaitingStatus.STAND_BY, LocalDateTime.now(), LocalDateTime.now().plusMinutes(30));
        List<ConcertSchedule> concertSchedules = new ArrayList<>();
        Concert concert = new Concert(1L, title, "부산문화회관 대극장");
        concertSchedules.add(new ConcertSchedule(1L, concert, now, LocalDateTime.now().minusDays(1), LocalDateTime.now().plusDays(1), 50));

        // when
        when(waitingRepository.findByToken(waitingToken)).thenReturn(waiting);
        when(concertScheduleRepository.findByConcertScheduleDates(any(LocalDateTime.class), any(Integer.class)))
                .thenReturn(concertSchedules);

        // then
        List<ConcertScheduleDto> result = concertService.loadConcertDate(waitingToken);

        // 결과 검증
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).scheduleId());
        assertEquals(title, result.get(0).concertTitle());
    }

    @Test
    public void 예약_가능_좌석_조회() {
        // given
        String waitingToken = "testToken";
        long scheduleId = 1L;

        // when
        List<ConcertSeatDto> result = concertService.loadConcertSeat(waitingToken, scheduleId);

        // then
        assertEquals(1, result.size());
        assertEquals("A01", result.get(0).seatNum());
        assertEquals(3000, result.get(0).amount());
        assertEquals(SeatStatus.STAND_BY, result.get(0).seatStatus());
    }

    @Test
    public void 좌석_예약_성공() {
        // given
        String waitingToken = "testToken";
        long seatId = 1L;

        // when
        PayDto result = concertService.processReserve(waitingToken, seatId);

        // then
        assertEquals(1L, result.reserveId());
        assertEquals(400, result.amount());
        assertEquals(false, result.isPay());
    }

}