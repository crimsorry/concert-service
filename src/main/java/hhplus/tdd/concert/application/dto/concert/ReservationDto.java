package hhplus.tdd.concert.application.dto.concert;

import hhplus.tdd.concert.application.dto.waiting.MemberDto;
import hhplus.tdd.concert.domain.entity.concert.*;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

public record ReservationDto(
        long reserveId,
        String memberName,
        String concertTitle,
        LocalDateTime openDate,
        String seatNum,
        Integer amount,
        SReserveStatus reserveStatus
) {
    public static ReservationDto from(Reservation reservation) {
        return new ReservationDto(
                reservation.getReserveId(),
                reservation.getMember().getMemberName(),
                reservation.getConcertTitle(),
                reservation.getOpenDate(),
                reservation.getSeatNum(),
                reservation.getAmount(),
                mapReserveStatusToSReserveStatus(reservation.getReserveStatus())
        );
    }

    private static SReserveStatus mapReserveStatusToSReserveStatus(ReserveStatus reserveStatus) {
        return switch (reserveStatus) {
            case RESERVED -> SReserveStatus.RESERVED;
            case PENDING -> SReserveStatus.PENDING;
            case CANCELED -> SReserveStatus.CANCELED;
        };
    }

}
