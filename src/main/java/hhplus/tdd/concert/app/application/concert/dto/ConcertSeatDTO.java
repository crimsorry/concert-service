package hhplus.tdd.concert.app.application.concert.dto;

import hhplus.tdd.concert.app.domain.concert.entity.ConcertSeat;
import hhplus.tdd.concert.config.types.SeatStatus;

import java.util.List;
import java.util.stream.Collectors;

public record ConcertSeatDTO(
        Long seatId,
        String seatCode,
        Integer amount,
        SeatStatus seatStatus
) {

    public static ConcertSeatDTO from(ConcertSeat concertSeat) {
        return new ConcertSeatDTO(
                concertSeat.getSeatId(),
                concertSeat.getSeatCode(),
                concertSeat.getAmount(),
                concertSeat.getSeatStatus()
        );
    }

    public static List<ConcertSeatDTO> from(List<ConcertSeat> concertSeats) {
        return concertSeats.stream()
                .map(ConcertSeatDTO::from)
                .collect(Collectors.toList());
    }

}
