package hhplus.tdd.concert.app.domain.event;

public interface KakaoProcessEventListener {
    void handleKakaoReservationProcessEvent(KakaoReservationProcessEvent event);
    void handleKakaoPayProcessEvent(KakaoPayProcessEvent event);
}