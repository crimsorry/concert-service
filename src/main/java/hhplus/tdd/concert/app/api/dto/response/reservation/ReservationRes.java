package hhplus.tdd.concert.app.api.dto.response.reservation;

import hhplus.tdd.concert.app.application.reservation.dto.ReservationDTO;
import hhplus.tdd.concert.app.application.reservation.dto.ReservationQuery;
import hhplus.tdd.concert.config.types.ReserveStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public record ReservationRes(
        long reserveId,
        String memberName,
        String concertTitle,
        LocalDateTime openDate,
        String seatCode,
        Integer amount,
        ReserveStatus reserveStatus
) {

    public static ReservationRes fromCommand(ReservationDTO dto) {
        return new ReservationRes(
                dto.reserveId(),
                dto.memberName(),
                dto.concertTitle(),
                dto.openDate(),
                dto.seatCode(),
                dto.amount(),
                dto.reserveStatus()
        );
    }

    public static List<ReservationRes> fromCommand(List<ReservationDTO> dto) {
        return dto.stream()
                .map(ReservationRes::fromCommand)
                .collect(Collectors.toList());
    }

    public static ReservationRes fromQuery(ReservationDTO dto) {
        return new ReservationRes(
                dto.reserveId(),
                dto.memberName(),
                dto.concertTitle(),
                dto.openDate(),
                dto.seatCode(),
                dto.amount(),
                dto.reserveStatus()
        );
    }

    public static List<ReservationRes> fromQuery(List<ReservationDTO> dto) {
        return dto.stream()
                .map(ReservationRes::fromQuery)
                .collect(Collectors.toList());
    }

}
