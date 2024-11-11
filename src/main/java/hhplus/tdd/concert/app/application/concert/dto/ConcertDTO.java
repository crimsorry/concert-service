package hhplus.tdd.concert.app.application.concert.dto;

import hhplus.tdd.concert.app.domain.concert.entity.Concert;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

public record ConcertDTO(
        Long concertId,
        String concertTitle,
        String concertPlace
) implements Serializable {

    private static final long serialVersionUID = 1L;

    public static ConcertDTO from(Concert concert) {
        return new ConcertDTO(
                concert.getConcertId(),
                concert.getConcertTitle(),
                concert.getConcertPlace()
        );
    }

    public static List<ConcertDTO> from(List<Concert> concerts) {
        return concerts.stream()
                .map(ConcertDTO::from)
                .collect(Collectors.toList());
    }

}
