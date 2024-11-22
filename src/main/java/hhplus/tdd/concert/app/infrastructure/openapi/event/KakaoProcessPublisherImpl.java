package hhplus.tdd.concert.app.infrastructure.openapi.event;

import hhplus.tdd.concert.app.domain.openapi.event.KakaoMsgEvent;
import hhplus.tdd.concert.app.domain.openapi.event.KakaoProcessPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KakaoProcessPublisherImpl implements KakaoProcessPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    private static final String KAKAO_TOTIC = "kakao-event";

    @Override
    public void publishEvent(KakaoMsgEvent kakaoMsgEvent) {
        log.info("알림톡 publish 진입");
        kafkaTemplate.send(KAKAO_TOTIC, kakaoMsgEvent);
    }
}
