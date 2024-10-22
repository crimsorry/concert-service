package hhplus.tdd.concert.app.api.dto.response.concert;

import hhplus.tdd.concert.app.application.dto.concert.ConcertSeatDto;
import hhplus.tdd.concert.common.types.SeatStatus;

public record ConcertSeatRes(
        Long seatId,
        String seatNum,
        Integer amount,
        SeatStatus seatStatus
) {
    public static ConcertSeatRes from(ConcertSeatDto concertSeatDto) {
        return new ConcertSeatRes(
                concertSeatDto.seatId(),
                concertSeatDto.seatNum(),
                concertSeatDto.amount(),
                concertSeatDto.seatStatus()
        );
    }
}
