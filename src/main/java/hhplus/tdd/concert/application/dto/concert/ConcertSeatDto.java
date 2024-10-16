package hhplus.tdd.concert.application.dto.concert;

public record ConcertSeatDto(
        Long seatId,
        String seatNum,
        Integer amount,
        SSeatStatus seatStatus
) {}
