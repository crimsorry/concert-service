package hhplus.tdd.concert.app.api.dto.response.concert;

import hhplus.tdd.concert.app.application.concert.dto.ConcertSeatQuery;
import hhplus.tdd.concert.config.types.SeatStatus;

public record ConcertSeatRes(
        Long seatId,
        String seatCode,
        Integer amount,
        SeatStatus seatStatus
) {
    public static ConcertSeatRes from(ConcertSeatQuery concertSeatQuery) {
        return new ConcertSeatRes(
                concertSeatQuery.seatId(),
                concertSeatQuery.seatCode(),
                concertSeatQuery.amount(),
                concertSeatQuery.seatStatus()
        );
    }
}
