package hhplus.tdd.concert.app.api.dto.response.concert;

import hhplus.tdd.concert.app.application.concert.dto.ConcertSeatDTO;
import hhplus.tdd.concert.config.types.SeatStatus;

public record ConcertSeatRes(
        Long seatId,
        String seatCode,
        Integer amount,
        SeatStatus seatStatus
) {
    public static ConcertSeatRes from(ConcertSeatDTO concertSeatDTO) {
        return new ConcertSeatRes(
                concertSeatDTO.seatId(),
                concertSeatDTO.seatCode(),
                concertSeatDTO.amount(),
                concertSeatDTO.seatStatus()
        );
    }
}
