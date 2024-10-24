package hhplus.tdd.concert.app.api.dto.response;

import org.springframework.boot.logging.LogLevel;

public record ErrorRes(
        String code,
        String message
) {
}