package hhplus.tdd.concert.common.config.exception;

import hhplus.tdd.concert.app.domain.exception.ErrorCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.boot.logging.LogLevel;

@Getter
@ToString
public class FailException extends RuntimeException {

    private final ErrorCode errorCode;
    private final LogLevel logLevel;

    public FailException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.logLevel = LogLevel.INFO;
    }

    // 동적 옵션
    public FailException(ErrorCode errorCode, LogLevel logLevel) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.logLevel = logLevel;
    }

}