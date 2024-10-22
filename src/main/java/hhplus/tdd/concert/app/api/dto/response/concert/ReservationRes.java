package hhplus.tdd.concert.app.api.dto.response.concert;

import hhplus.tdd.concert.app.application.dto.concert.ReservationDto;
import hhplus.tdd.concert.common.types.ReserveStatus;

import java.time.LocalDateTime;

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

}
