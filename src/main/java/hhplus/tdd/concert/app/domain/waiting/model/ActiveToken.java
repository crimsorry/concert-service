package hhplus.tdd.concert.app.domain.waiting.model;

import java.time.LocalDateTime;

public record ActiveToken(
        Long memberId,
        String token,
        LocalDateTime expiredAt
) {}
