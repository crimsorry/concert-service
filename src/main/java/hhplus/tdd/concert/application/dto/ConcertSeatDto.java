package hhplus.tdd.concert.application.dto;

import hhplus.tdd.concert.domain.enums.SeatStatus;

public record ConcertSeatDto(
        Long seatId,
        String seatNum,
        Integer amount,
        SeatStatus seatStatus
) {}
