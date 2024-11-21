package hhplus.tdd.concert.app.application.openapi.event;

import hhplus.tdd.concert.app.application.openapi.service.OpenapiService;
import hhplus.tdd.concert.app.domain.openapi.event.KakaoPayProcessEvent;
import hhplus.tdd.concert.app.domain.openapi.event.KakaoProcessEventListener;
import hhplus.tdd.concert.app.domain.openapi.event.KakaoReservationProcessEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class KakaoProcessEventListenerImpl implements KakaoProcessEventListener {

    private static final String KAKAO_RESERVATION_TOTIC = "kakao_reservation_event";
    private static final String KAKAO_PAY_TOPIC = "kakao_pay_event";

    private final OpenapiService openapiService;

    @KafkaListener(topics = KAKAO_RESERVATION_TOTIC,
            groupId = "kakao-group",
            errorHandler = "deadLetterQueueKakaoErrorHandler")
    @Async
    @Override
    public void handleKakaoReservationProcessEvent(KakaoReservationProcessEvent event) {
        openapiService.processKakaoMsgReservation(event.getReservationDTO());
    }

    @KafkaListener(topics = KAKAO_PAY_TOPIC,
            groupId = "kakao-group",
            errorHandler = "deadLetterQueueKakaoErrorHandler")
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Override
    public void handleKakaoPayProcessEvent(KakaoPayProcessEvent event) {
        openapiService.processKakaoMsgPay(event.getPayDTO());
    }
}