package hhplus.tdd.concert.application.dto;

import hhplus.tdd.concert.domain.enums.ReserveStatus;

import java.time.LocalDateTime;

public record ReservationDto(
        long reserveId,
        UserDto user,
        String concertTitle,
        LocalDateTime openDate,
        String seatNum,
        Integer amount,
        ReserveStatus reserveStatus
) {
}
