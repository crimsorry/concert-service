package hhplus.tdd.concert.app.api.dto.response.concert;

import hhplus.tdd.concert.app.application.concert.dto.ConcertDTO;

public record ConcertRes(
        Long concertId,
        String concertTitle,
        String concertPlace
) {

    public static ConcertRes from(ConcertDTO concertDTO) {
        return new ConcertRes(
                concertDTO.concertId(),
                concertDTO.concertTitle(),
                concertDTO.concertPlace()
        );
    }
}
