package hhplus.tdd.concert.interfaces.api.dto.response;

import hhplus.tdd.concert.application.dto.ConcertSeatDto;
import hhplus.tdd.concert.application.dto.SSeatStatus;

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
