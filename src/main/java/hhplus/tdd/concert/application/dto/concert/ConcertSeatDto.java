package hhplus.tdd.concert.application.dto.concert;

import hhplus.tdd.concert.domain.entity.concert.ConcertSchedule;
import hhplus.tdd.concert.domain.entity.concert.ConcertSeat;
import hhplus.tdd.concert.domain.entity.concert.SeatStatus;

import java.util.List;
import java.util.stream.Collectors;

public record ConcertSeatDto(
        Long seatId,
        String seatNum,
        Integer amount,
        SSeatStatus seatStatus
) {

    public static ConcertSeatDto from(ConcertSeat concertSeat) {
        return new ConcertSeatDto(
                concertSeat.getSeatId(),
                concertSeat.getSeatNum(),
                concertSeat.getAmount(),
                mapSeatStatusToSSeatStatus(concertSeat.getSeatStatus())
        );
    }

    public static List<ConcertSeatDto> from(List<ConcertSeat> concertSeats) {
        return concertSeats.stream()
                .map(ConcertSeatDto::from)
                .collect(Collectors.toList());
    }

    private static SSeatStatus mapSeatStatusToSSeatStatus(SeatStatus SeatStatus) {
        return switch (SeatStatus) {
            case STAND_BY -> SSeatStatus.STAND_BY;
            case RESERVED -> SSeatStatus.RESERVED;
            case ASSIGN -> SSeatStatus.ASSIGN;
        };
    }

}
