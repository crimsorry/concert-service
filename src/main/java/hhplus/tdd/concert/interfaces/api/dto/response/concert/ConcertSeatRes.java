package hhplus.tdd.concert.interfaces.api.dto.response.concert;

import hhplus.tdd.concert.application.dto.concert.ConcertSeatDto;
import hhplus.tdd.concert.application.dto.concert.SSeatStatus;

public record ConcertSeatRes(
        Long seatId,
        String seatNum,
        Integer amount,
        CSeatStatus seatStatus
) {
    public static ConcertSeatRes from(ConcertSeatDto concertSeatDto) {
        return new ConcertSeatRes(
                concertSeatDto.seatId(),
                concertSeatDto.seatNum(),
                concertSeatDto.amount(),
                mapSSeatStatusToCSeatStatus(concertSeatDto.seatStatus())
        );
    }

    private static CSeatStatus mapSSeatStatusToCSeatStatus(SSeatStatus sSeatStatus) {
        return switch (sSeatStatus) {
            case STAND_BY -> CSeatStatus.STAND_BY;
            case RESERVED -> CSeatStatus.RESERVED;
            case ASSIGN -> CSeatStatus.ASSIGN;
        };
    }
}
