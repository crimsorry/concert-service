package hhplus.tdd.concert.app.domain.openapi.event;

public interface KakaoProcessPublisher {

    void publishEvent(KakaoMsgEvent kakaoMsgEvent);
}
