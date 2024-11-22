package hhplus.tdd.concert.app.application.openapi.event;

import hhplus.tdd.concert.app.application.openapi.dto.KakaoMsgDto;
import hhplus.tdd.concert.app.application.openapi.service.OpenapiService;
import hhplus.tdd.concert.app.domain.openapi.event.KakaoMsgEvent;
import hhplus.tdd.concert.app.domain.openapi.event.KakaoProcessEventListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KakaoProcessEventListenerImpl implements KakaoProcessEventListener {

    private static final String KAKAO_TOTIC = "kakao-event";

    private final OpenapiService openapiService;

    @KafkaListener(topics = KAKAO_TOTIC,
            groupId = "kakao-group",
            errorHandler = "deadLetterQueueKakaoErrorHandler")
    @Async
    @Override
    public void handleKakaoProcessEvent(KakaoMsgEvent kakaoMsgEvent) {
        log.info("알림톡 listener 진입");
        openapiService.processKakaoMsg(KakaoMsgDto.from(kakaoMsgEvent));
    }
}