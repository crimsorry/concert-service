package hhplus.tdd.concert.application.dto;

import java.time.LocalDateTime;

public record PayDto(
        long payId,
        long userId,
        long reserveId,
        int amount,
        boolean isPay,
        LocalDateTime createAt
) {
}
