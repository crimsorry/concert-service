package hhplus.tdd.concert.domain.exception;

public enum ErrorCode {

    MEMBER_NOT_FOUND("E100", "존재하지 않는 사용자입니다."),
    WAITING_MEMBER_NOT_FOUND("E101", "사용자 확인 불가. 대기열 토큰을 발급받아 주세요."),
    EXPIRED_WAITING_TOKEN("E102", "만료된 토큰. 대기열 토큰을 반급받아 주세요."),
    ASSIGN_SEAT("E200", "이미 선택된 좌석입니다."),
    RESERVED_SEAT("E201", "이미 임시배정된 좌석입니다."),
    SUCCESS_PAY_SEAT("E203", "이미 결제 완료된 좌석입니다."),
    NOT_FOUND_CONCERT_SCHEDULE("E204", "존재하지 않는 콘서트 스케줄입니다."),
    NOT_FOUND_CONCERT_SEAT("E205", "존재하지 않는 좌석입니다."),
    AMNIOTIC_PAY("E300", "양수 값만 충전 가능합니다."),
    FULL_PAY("E301", "충전 한도 조과. (한도: 100만원)"),
    EMPTY_PAY("E302", "잔액이 부족합니다.");

    private final String code;
    private final String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
