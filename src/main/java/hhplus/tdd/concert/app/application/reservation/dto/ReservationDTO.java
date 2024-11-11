package hhplus.tdd.concert.app.application.reservation.dto;

import hhplus.tdd.concert.app.domain.reservation.entity.Reservation;
import hhplus.tdd.concert.config.types.ReserveStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public record ReservationDTO(
        long reserveId,
        String memberName,
        String concertTitle,
        LocalDateTime openDate,
        String seatCode,
        Integer amount,
        ReserveStatus reserveStatus
) {
    public static ReservationDTO from(Reservation reservation) {
        return new ReservationDTO(
                reservation.getReserveId(),
                reservation.getMember().getMemberName(),
                reservation.getConcertTitle(),
                reservation.getOpenDate(),
                reservation.getSeatCode(),
                reservation.getAmount(),
                reservation.getReserveStatus()
        );
    }

    public static List<ReservationDTO> from(List<Reservation> reservations) {
        return reservations.stream()
                .map(ReservationDTO::from)
                .collect(Collectors.toList());
    }

}
