package hhplus.tdd.concert.app.application.dto.reservation;

import hhplus.tdd.concert.app.application.dto.concert.ConcertScheduleDto;
import hhplus.tdd.concert.app.domain.entity.concert.ConcertSchedule;
import hhplus.tdd.concert.app.domain.entity.concert.Reservation;
import hhplus.tdd.concert.common.types.ReserveStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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

    public static List<ReservationDto> from(List<Reservation> reservations) {
        return reservations.stream()
                .map(ReservationDto::from)
                .collect(Collectors.toList());
    }

}
