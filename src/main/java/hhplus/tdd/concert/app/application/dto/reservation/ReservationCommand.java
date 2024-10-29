package hhplus.tdd.concert.app.application.dto.reservation;

import hhplus.tdd.concert.app.domain.entity.reservation.Reservation;
import hhplus.tdd.concert.common.types.ReserveStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public record ReservationCommand(
        long reserveId,
        String memberName,
        String concertTitle,
        LocalDateTime openDate,
        String seatCode,
        Integer amount,
        ReserveStatus reserveStatus
) {
    public static ReservationCommand from(Reservation reservation) {
        return new ReservationCommand(
                reservation.getReserveId(),
                reservation.getMember().getMemberName(),
                reservation.getConcertTitle(),
                reservation.getOpenDate(),
                reservation.getSeatCode(),
                reservation.getAmount(),
                reservation.getReserveStatus()
        );
    }

    public static List<ReservationCommand> from(List<Reservation> reservations) {
        return reservations.stream()
                .map(ReservationCommand::from)
                .collect(Collectors.toList());
    }

}
