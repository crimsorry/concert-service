package hhplus.tdd.concert.application.dto;

import hhplus.tdd.concert.domain.entity.concert.SeatStatus;

public record ConcertSeatDto(
        Long seatId,
        String seatNum,
        Integer amount,
        SSeatStatus seatStatus
) {}
