package hhplus.tdd.concert.interfaces.api.dto.response.concert;

import hhplus.tdd.concert.application.dto.concert.ReservationDto;
import hhplus.tdd.concert.application.dto.concert.SReserveStatus;
import hhplus.tdd.concert.domain.entity.concert.ReserveStatus;

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
                mapToSStatus(dto.reserveStatus())
        );
    }

    private static ReserveStatus mapToSStatus(SReserveStatus sReserveStatus) {
        return switch (sReserveStatus) {
            case PENDING -> ReserveStatus.PENDING;
            case RESERVED -> ReserveStatus.RESERVED;
            case CANCELED -> ReserveStatus.CANCELED;
        };
    }

}
