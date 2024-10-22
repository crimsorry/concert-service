package hhplus.tdd.concert.app.application.dto.concert;

import hhplus.tdd.concert.app.domain.entity.concert.ConcertSeat;
import hhplus.tdd.concert.common.types.SeatStatus;

import java.util.List;
import java.util.stream.Collectors;

public record ConcertSeatDto(
        Long seatId,
        String seatNum,
        Integer amount,
        SeatStatus seatStatus
) {

    public static ConcertSeatDto from(ConcertSeat concertSeat) {
        return new ConcertSeatDto(
                concertSeat.getSeatId(),
                concertSeat.getSeatNum(),
                concertSeat.getAmount(),
                concertSeat.getSeatStatus()
        );
    }

    public static List<ConcertSeatDto> from(List<ConcertSeat> concertSeats) {
        return concertSeats.stream()
                .map(ConcertSeatDto::from)
                .collect(Collectors.toList());
    }

}
