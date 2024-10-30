package hhplus.tdd.concert.app.domain.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {

    NOT_FOUNT_MEMBER("존재하지 않는 사용자입니다."),
    NOT_FOUND_WAITING_MEMBER("사용자 확인 불가. 대기열 토큰을 발급받아 주세요."),
    EXPIRED_WAITING_TOKEN("만료된 토큰. 대기열 토큰을 반급받아 주세요."),
    WAITING_MEMBER("대기 중인 사용자입니다."),

    ASSIGN_SEAT("이미 선택된 좌석입니다."),
    RESERVED_SEAT( "이미 임시배정된 좌석입니다."),
    SUCCESS_PAY_SEAT( "이미 결제 완료된 좌석입니다."),
    NOT_FOUND_CONCERT_SCHEDULE( "존재하지 않는 콘서트 스케줄입니다."),
    NOT_FOUND_CONCERT_SEAT("존재하지 않는 좌석입니다."),
    NOT_FOUND_SEAT_RESERVED( "임시배정된 좌석이 존재하지 않습니다."),

    AMNIOTIC_PAY( "양수 값만 충전 가능합니다."),
    FULL_PAY( "충전 한도 조과. (한도: 1000만원)"),
    EMPTY_PAY( "잔액이 부족합니다."),
    NOT_FOUNT_PAYMENT( "존재하지 않는 결제 입니다."),

    DATABASE_CHANGE_OPTIMISTIC_LOCK("다른 사용자가 먼저 데이터를 변경했습니다."),

    REDIS_LOCK_NOT_AVAILABLE("REDIS - 락을 획득하지 못했습니다. (대기시간 초가 등)"),
    REDIS_LOCK_INTERRUPTED("REDIS - 인터럽트 에러가 발생했습니다.");

    private final String message;

    ErrorCode(String message) {
        this.message = message;
    }
}
