package hhplus.tdd.concert.app.domain.openapi.event;

public interface KakaoProcessEventListener {
    void handleKakaoProcessEvent(KakaoMsgEvent kakaoMsgEvent);
}