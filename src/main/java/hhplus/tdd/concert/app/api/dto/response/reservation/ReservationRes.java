package hhplus.tdd.concert.app.api.dto.response.reservation;

import hhplus.tdd.concert.app.application.dto.reservation.ReservationDto;
import hhplus.tdd.concert.common.types.ReserveStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public record ReservationRes(
        long reserveId,
        String memberName,
        String concertTitle,
        LocalDateTime openDate,
        String seatNum,
        Integer amount,
        ReserveStatus reserveStatus
) {

    public static ReservationRes from(ReservationDto dto) {
        return new ReservationRes(
                dto.reserveId(),
                dto.memberName(),
                dto.concertTitle(),
                dto.openDate(),
                dto.seatNum(),
                dto.amount(),
                dto.reserveStatus()
        );
    }

    public static List<ReservationRes> from(List<ReservationDto> dto) {
        return dto.stream()
                .map(ReservationRes::from)
                .collect(Collectors.toList());
    }

}
