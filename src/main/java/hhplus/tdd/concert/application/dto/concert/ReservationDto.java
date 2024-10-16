package hhplus.tdd.concert.application.dto.concert;

import hhplus.tdd.concert.application.dto.userqueue.UserDto;

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
