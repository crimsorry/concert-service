package hhplus.tdd.concert.app.application.waiting.event;

import hhplus.tdd.concert.app.application.waiting.service.WaitingService;
import hhplus.tdd.concert.app.domain.waiting.event.WaitingExpiredEvent;
import hhplus.tdd.concert.app.domain.waiting.event.WaitingExpiredEventListener;
import hhplus.tdd.concert.app.domain.waiting.entity.ActiveToken;
import hhplus.tdd.concert.app.domain.waiting.event.WaitingExpiredTimeEvent;
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
public class WaitingExpiredEventListenerImpl implements WaitingExpiredEventListener {

    private final WaitingService waitingService;

    private static final String WAITING_EXPIRED_TOPIC = "waiting_expired_event";
    private static final String WAITING_EXPIRED_TIME_TOPIC = "waiting_expired_time_event";

    @KafkaListener(topics = WAITING_EXPIRED_TOPIC,
            groupId = "waiting-group",
            errorHandler = "deadLetterQueueErrorHandler")
    @Async
    @Override
    public void handleWaitingExpiredEvent(WaitingExpiredEvent waitingExpiredEvent) {
        try{
            waitingService.deleteActiveToken(waitingExpiredEvent.getActiveToken());
            log.info("대기열 만료 성공");
        }catch (Exception e){
            log.info("대기열 만료 실패");
        }
    }

    @KafkaListener(topics = WAITING_EXPIRED_TIME_TOPIC,
            groupId = "waiting-group",
            errorHandler = "deadLetterQueueErrorHandler")
    @Async
    @Override
    public void handleWaitingExpiredTimeEvent(WaitingExpiredTimeEvent waitingExpiredTimeEvent) {
        try{
            waitingService.updateActiveToken(waitingExpiredTimeEvent.getValue());
            log.info("대기열 상태 업데이트 성공");
        }catch (Exception e){
            log.info("대기열 상태 업데이트 실패");
        }
    }

}