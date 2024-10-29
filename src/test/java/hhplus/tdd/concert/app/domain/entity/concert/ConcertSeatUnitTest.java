package hhplus.tdd.concert.app.domain.entity.concert;

import hhplus.tdd.concert.app.domain.exception.ErrorCode;
import hhplus.tdd.concert.common.config.exception.FailException;
import hhplus.tdd.concert.common.types.SeatStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ConcertSeatUnitTest {

    @Test
    public void 존재_하지_않는_콘서트_좌석(){
        // given
        ConcertSeat concertSeat = null;

        // when & then
        Exception exception = assertThrows(FailException.class, () -> {
            ConcertSeat.checkConcertSeatExistence(concertSeat);
        });

        // 결과 검증
        assertEquals(ErrorCode.NOT_FOUND_CONCERT_SEAT.getMessage(), exception.getMessage());
    }

    @Test
    public void 예약_아닌_콘서트_좌석(){
        // given
        ConcertSeat concertSeat = ConcertSeat.builder()
                .seatStatus(SeatStatus.ASSIGN)
                .build();

        // when & then
        Exception exception = assertThrows(FailException.class, () -> {
            ConcertSeat.checkConcertSeatReserved(concertSeat);
        });

        // 결과 검증
        assertEquals(ErrorCode.NOT_FOUND_SEAT_RESERVED.getMessage(), exception.getMessage());
    }

    @Test
    public void 콘서트_좌석_상태_예약_완료(){
        // given
        ConcertSeat concertSeat = ConcertSeat.builder()
                .seatStatus(SeatStatus.ASSIGN)
                .build();

        // when & then
        Exception exception = assertThrows(FailException.class, () -> {
            ConcertSeat.checkConcertSeatStatus(concertSeat);
        });

        // 결과 검증
        assertEquals(ErrorCode.ASSIGN_SEAT.getMessage(), exception.getMessage());
    }

    @Test
    public void 콘서트_좌석_상태_임시배정(){
        // given
        ConcertSeat concertSeat = ConcertSeat.builder()
                .seatStatus(SeatStatus.RESERVED)
                .build();

        // when & then
        Exception exception = assertThrows(FailException.class, () -> {
            ConcertSeat.checkConcertSeatStatus(concertSeat);
        });

        // 결과 검증
        assertEquals(ErrorCode.RESERVED_SEAT.getMessage(), exception.getMessage());
    }

    @Test
    public void 좌석_선택_가능_상태(){
        // given
        ConcertSeat concertSeat = ConcertSeat.builder()
                .seatStatus(SeatStatus.RESERVED)
                .build();

        // when & then
        concertSeat.open();

        // 결과 검증
        assertEquals(SeatStatus.STAND_BY, concertSeat.getSeatStatus());
    }

    @Test
    public void 좌석_임시_배정_상태(){
        // given
        ConcertSeat concertSeat = ConcertSeat.builder()
                .seatStatus(SeatStatus.STAND_BY)
                .build();

        // when & then
        concertSeat.pending();

        // 결과 검증
        assertEquals(SeatStatus.RESERVED, concertSeat.getSeatStatus());
    }

    @Test
    public void 좌석_할당된_상태(){
        // given
        ConcertSeat concertSeat = ConcertSeat.builder()
                .seatStatus(SeatStatus.RESERVED)
                .build();

        // when & then
        concertSeat.close();

        // 결과 검증
        assertEquals(SeatStatus.ASSIGN, concertSeat.getSeatStatus());
    }
}
