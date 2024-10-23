package hhplus.tdd.concert.app.api.dto.response.concert;

import hhplus.tdd.concert.app.application.dto.concert.ConcertSeatQuery;
import hhplus.tdd.concert.common.types.SeatStatus;

public record ConcertSeatRes(
        Long seatId,
        String seatNum,
        Integer amount,
        SeatStatus seatStatus
) {
    public static ConcertSeatRes from(ConcertSeatQuery concertSeatQuery) {
        return new ConcertSeatRes(
                concertSeatQuery.seatId(),
                concertSeatQuery.seatNum(),
                concertSeatQuery.amount(),
                concertSeatQuery.seatStatus()
        );
    }
}
