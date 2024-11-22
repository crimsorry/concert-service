package hhplus.tdd.concert.app.interfaces.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record ConcertReserveReq(
        @Schema(description = "콘서트 좌석 ID")
        long seatId
) {
}
