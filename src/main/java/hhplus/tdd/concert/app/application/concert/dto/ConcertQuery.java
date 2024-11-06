package hhplus.tdd.concert.app.application.concert.dto;

import hhplus.tdd.concert.app.domain.concert.entity.Concert;
import hhplus.tdd.concert.app.domain.concert.entity.ConcertSchedule;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public record ConcertQuery(
        Long concertId,
        String concertTitle,
        String concertPlace
) implements Serializable {

    private static final long serialVersionUID = 1L;

    public static ConcertQuery from(Concert concert) {
        return new ConcertQuery(
                concert.getConcertId(),
                concert.getConcertTitle(),
                concert.getConcertPlace()
        );
    }

    public static List<ConcertQuery> from(List<Concert> concerts) {
        return concerts.stream()
                .map(ConcertQuery::from)
                .collect(Collectors.toList());
    }

}
