package hhplus.tdd.concert.domain.entity.concert;

import hhplus.tdd.concert.app.domain.entity.concert.ConcertSeat;
import hhplus.tdd.concert.common.types.SeatStatus;
import hhplus.tdd.concert.app.domain.exception.ErrorCode;
import hhplus.tdd.concert.common.config.FailException;
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
        ConcertSeat concertSeat = new ConcertSeat();
        concertSeat.setSeatStatus(SeatStatus.ASSIGN);

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
        ConcertSeat concertSeat = new ConcertSeat();
        concertSeat.setSeatStatus(SeatStatus.ASSIGN);

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
        ConcertSeat concertSeat = new ConcertSeat();
        concertSeat.setSeatStatus(SeatStatus.RESERVED);

        // when & then
        Exception exception = assertThrows(FailException.class, () -> {
            ConcertSeat.checkConcertSeatStatus(concertSeat);
        });

        // 결과 검증
        assertEquals(ErrorCode.RESERVED_SEAT.getMessage(), exception.getMessage());
    }
}
