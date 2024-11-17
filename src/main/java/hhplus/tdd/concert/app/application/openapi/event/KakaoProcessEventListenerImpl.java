package hhplus.tdd.concert.app.application.openapi.event;

import hhplus.tdd.concert.app.application.openapi.service.OpenapiService;
import hhplus.tdd.concert.app.domain.openapi.event.KakaoPayProcessEvent;
import hhplus.tdd.concert.app.domain.openapi.event.KakaoProcessEventListener;
import hhplus.tdd.concert.app.domain.openapi.event.KakaoReservationProcessEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class KakaoProcessEventListenerImpl implements KakaoProcessEventListener {

    private final OpenapiService openapiService;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Override
    public void handleKakaoReservationProcessEvent(KakaoReservationProcessEvent event) {
        openapiService.processKakaoMsgReservation(event.getReservationDTO());
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Override
    public void handleKakaoPayProcessEvent(KakaoPayProcessEvent event) {
        openapiService.processKakaoMsgPay(event.getPayDTO());
    }
}