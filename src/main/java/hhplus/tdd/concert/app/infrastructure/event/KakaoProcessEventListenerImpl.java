package hhplus.tdd.concert.app.infrastructure.event;

import hhplus.tdd.concert.app.domain.event.KakaoPayProcessEvent;
import hhplus.tdd.concert.app.domain.event.KakaoProcessEventListener;
import hhplus.tdd.concert.app.domain.event.KakaoReservationProcessEvent;
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

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Override
    public void handleKakaoReservationProcessEvent(KakaoReservationProcessEvent event) {
        try {
            // 카카오톡 메시지 전송 로직
            log.info("Sending Kakao message for reservation: " + event.getReservationDTO());
        } catch (Exception e) {
            // 실패 처리 로직
            log.warn("Failed to send Kakao message: " + e.getMessage());
        }
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Override
    public void handleKakaoPayProcessEvent(KakaoPayProcessEvent event) {
        try {
            // 카카오톡 메시지 전송 로직
            log.info("Sending Kakao message for reservation: " + event.getPayDTO());
        } catch (Exception e) {
            // 실패 처리 로직
            log.warn("Failed to send Kakao message: " + e.getMessage());
        }
    }
}