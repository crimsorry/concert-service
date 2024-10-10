package hhplus.tdd.concert.interfaces.api.dto.response;

import hhplus.tdd.concert.application.dto.ReservationDto;
import hhplus.tdd.concert.domain.enums.ReserveStatus;

import java.time.LocalDateTime;

public record ReservationRes(
        long reserveId,
        UserRes user,
        String concertTitle,
        LocalDateTime openDate,
        String seatNum,
        Integer amount,
        ReserveStatus reserveStatus
) {

    public static ReservationRes from(ReservationDto dto) {
        return new ReservationRes(
                dto.reserveId(),
                UserRes.from(dto.user()),  // UserDto -> UserRes로 변환
                dto.concertTitle(),
                dto.openDate(),
                dto.seatNum(),
                dto.amount(),
                dto.reserveStatus()
        );
    }

}
