package hhplus.tdd.concert.domain.entity.concert;

import hhplus.tdd.concert.domain.entity.member.Member;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReservationUnitTest {

    private final LocalDateTime now = LocalDateTime.now();
    private final Member member = new Member(1L, "김소리", 0);
    private final Concert concert = new Concert(1L, "드라큘라", "부산문화회관 대극장");
    private final ConcertSchedule concertSchedule = new ConcertSchedule(1L, concert, now, LocalDateTime.now().minusDays(1), LocalDateTime.now().plusDays(1), 50);
    private final ConcertSeat concertSeat = new ConcertSeat(1L, concertSchedule, "A01", 140000, SeatStatus.STAND_BY);

    @Test
    public void 예약_빌더() {
        // when & then
        Reservation reservation = Reservation.generateReservation(member, concertSeat);

        // 결과 검증
        assertEquals(member, reservation.getMember());
        assertEquals("드라큘라", reservation.getConcertTitle());
    }

}
