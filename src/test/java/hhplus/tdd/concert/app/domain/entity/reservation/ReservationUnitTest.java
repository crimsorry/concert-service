package hhplus.tdd.concert.app.domain.entity.reservation;

import hhplus.tdd.concert.app.domain.concert.entity.Concert;
import hhplus.tdd.concert.app.domain.concert.entity.ConcertSchedule;
import hhplus.tdd.concert.app.domain.concert.entity.ConcertSeat;
import hhplus.tdd.concert.app.domain.waiting.entity.Member;
import hhplus.tdd.concert.app.domain.reservation.entity.Reservation;
import hhplus.tdd.concert.config.types.ReserveStatus;
import hhplus.tdd.concert.config.types.SeatStatus;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReservationUnitTest {

    private final LocalDateTime now = LocalDateTime.now();
    private final Member member = Member.builder().memberName("김소리").build();
    private final Concert concert = new Concert(1L, "드라큘라", "부산문화회관 대극장");
    private final ConcertSchedule concertSchedule = new ConcertSchedule(1L, concert, now, LocalDateTime.now().minusDays(1), LocalDateTime.now().plusDays(1));
    private final ConcertSeat concertSeat = ConcertSeat.builder().seatId(1L).schedule(concertSchedule).seatCode("A01").amount(140000).seatStatus(SeatStatus.STAND_BY).build();

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
