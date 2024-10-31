package hhplus.tdd.concert.app.application.reservation.dto;

import hhplus.tdd.concert.app.domain.reservation.entity.Reservation;
import hhplus.tdd.concert.common.types.ReserveStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public record ReservationQuery(
        long reserveId,
        String memberName,
        String concertTitle,
        LocalDateTime openDate,
        String seatCode,
        Integer amount,
        ReserveStatus reserveStatus
) {
    public static ReservationQuery from(Reservation reservation) {
        return new ReservationQuery(
                reservation.getReserveId(),
                reservation.getMember().getMemberName(),
                reservation.getConcertTitle(),
                reservation.getOpenDate(),
                reservation.getSeatCode(),
                reservation.getAmount(),
                reservation.getReserveStatus()
        );
    }

    public static List<ReservationQuery> from(List<Reservation> reservations) {
        return reservations.stream()
                .map(ReservationQuery::from)
                .collect(Collectors.toList());
    }

}
