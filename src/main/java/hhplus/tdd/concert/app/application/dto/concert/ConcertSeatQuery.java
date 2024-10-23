package hhplus.tdd.concert.app.application.dto.concert;

import hhplus.tdd.concert.app.domain.entity.concert.ConcertSeat;
import hhplus.tdd.concert.common.types.SeatStatus;

import java.util.List;
import java.util.stream.Collectors;

public record ConcertSeatQuery(
        Long seatId,
        String seatNum,
        Integer amount,
        SeatStatus seatStatus
) {

    public static ConcertSeatQuery from(ConcertSeat concertSeat) {
        return new ConcertSeatQuery(
                concertSeat.getSeatId(),
                concertSeat.getSeatNum(),
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
