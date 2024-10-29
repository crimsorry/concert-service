package hhplus.tdd.concert.app.domain.entity.reservation;

import hhplus.tdd.concert.app.domain.entity.concert.Concert;
import hhplus.tdd.concert.app.domain.entity.concert.ConcertSchedule;
import hhplus.tdd.concert.app.domain.entity.concert.ConcertSeat;
import hhplus.tdd.concert.app.domain.entity.member.Member;
import hhplus.tdd.concert.common.types.ReserveStatus;
import hhplus.tdd.concert.common.types.SeatStatus;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReservationUnitTest {

    private final LocalDateTime now = LocalDateTime.now();
    private final Member member = new Member(1L, "김소리", 0);
    private final Concert concert = new Concert(1L, "드라큘라", "부산문화회관 대극장");
    private final ConcertSchedule concertSchedule = new ConcertSchedule(1L, concert, now, LocalDateTime.now().minusDays(1), LocalDateTime.now().plusDays(1));
    private final ConcertSeat concertSeat = new ConcertSeat(1L, concertSchedule, "A01", 140000, SeatStatus.STAND_BY);

    @Test
    public void 예약_빌더() {
        // when & then
        Reservation reservation = Reservation.generateReservation(member, concertSeat);

        // 결과 검증
        assertEquals(member, reservation.getMember());
        assertEquals("드라큘라", reservation.getConcertTitle());
    }

    @Test
    public void 예약_완료_상태(){
        // given
        Reservation reservation = Reservation.builder()
                .reserveStatus(ReserveStatus.PENDING)
                .build();

        // when & then
        reservation.complete();

        // 결과 검증
        assertEquals(ReserveStatus.RESERVED, reservation.getReserveStatus());
    }

    @Test
    public void 예약_취소_상태(){
        // given
        Reservation reservation = Reservation.builder()
                .reserveStatus(ReserveStatus.PENDING)
                .build();

        // when & then
        reservation.cancel();

        // 결과 검증
        assertEquals(ReserveStatus.CANCELED, reservation.getReserveStatus());
    }
}
