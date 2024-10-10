package hhplus.tdd.concert.interfaces.api.dto.request;

public record ConcertReserveReq(
        long userId,
        long seatId
) {
}
