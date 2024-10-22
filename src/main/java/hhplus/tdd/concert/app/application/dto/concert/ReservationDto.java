package hhplus.tdd.concert.app.application.dto.concert;

import hhplus.tdd.concert.app.domain.entity.concert.Reservation;
import hhplus.tdd.concert.common.types.ReserveStatus;

import java.time.LocalDateTime;

public record ReservationDto(
        long reserveId,
        String memberName,
        String concertTitle,
        LocalDateTime openDate,
        String seatNum,
        Integer amount,
        ReserveStatus reserveStatus
) {
    public static ReservationDto from(Reservation reservation) {
        return new ReservationDto(
                reservation.getReserveId(),
                reservation.getMember().getMemberName(),
                reservation.getConcertTitle(),
                reservation.getOpenDate(),
                reservation.getSeatNum(),
                reservation.getAmount(),
                reservation.getReserveStatus()
        );
    }

}
