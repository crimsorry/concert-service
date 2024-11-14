package hhplus.tdd.concert.app.domain.openapi.event;

public interface KakaoProcessEventListener {
    void handleKakaoReservationProcessEvent(KakaoReservationProcessEvent event);
    void handleKakaoPayProcessEvent(KakaoPayProcessEvent event);
}