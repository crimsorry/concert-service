package hhplus.tdd.concert.app.application.concert.dto;

import hhplus.tdd.concert.app.domain.concert.entity.ConcertSeat;
import hhplus.tdd.concert.common.types.SeatStatus;

import java.util.List;
import java.util.stream.Collectors;

public record ConcertSeatQuery(
        Long seatId,
        String seatCode,
        Integer amount,
        SeatStatus seatStatus
) {

    public static ConcertSeatQuery from(ConcertSeat concertSeat) {
        return new ConcertSeatQuery(
                concertSeat.getSeatId(),
                concertSeat.getSeatCode(),
                concertSeat.getAmount(),
                concertSeat.getSeatStatus()
        );
    }

    public static List<ConcertSeatQuery> from(List<ConcertSeat> concertSeats) {
        return concertSeats.stream()
                .map(ConcertSeatQuery::from)
                .collect(Collectors.toList());
    }

}
