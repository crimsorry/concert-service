package hhplus.tdd.concert.app.api.dto.response.concert;

import hhplus.tdd.concert.app.application.concert.dto.ConcertQuery;
import hhplus.tdd.concert.app.application.concert.dto.ConcertScheduleQuery;

import java.time.LocalDateTime;

public record ConcertRes(
        Long concertId,
        String concertTitle,
        String concertPlace
) {

    public static ConcertRes from(ConcertQuery concertQuery) {
        return new ConcertRes(
                concertQuery.concertId(),
                concertQuery.concertTitle(),
                concertQuery.concertPlace()
        );
    }
}
