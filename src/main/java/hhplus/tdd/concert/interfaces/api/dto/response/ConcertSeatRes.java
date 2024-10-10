package hhplus.tdd.concert.interfaces.api.dto.response;

import hhplus.tdd.concert.application.dto.ConcertSeatDto;
import hhplus.tdd.concert.domain.enums.SeatStatus;

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
