package hhplus.tdd.concert.application.dto;

import hhplus.tdd.concert.domain.entity.concert.ReserveStatus;

import java.time.LocalDateTime;

public record ReservationDto(
        long reserveId,
        UserDto user,
        String concertTitle,
        LocalDateTime openDate,
        String seatNum,
        Integer amount,
        SReserveStatus reserveStatus
) {
}
