package hhplus.tdd.concert.domain.exception;

public class FailException extends RuntimeException {

    private final ErrorCode errorCode;

    public FailException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    @Override
    public String toString() {
        return String.format("ErrorCode: %s, Message: %s", errorCode.getCode(), errorCode.getMessage());
    }
}