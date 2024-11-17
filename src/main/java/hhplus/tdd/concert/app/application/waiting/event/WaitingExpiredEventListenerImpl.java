package hhplus.tdd.concert.app.application.waiting.event;

import hhplus.tdd.concert.app.application.waiting.service.WaitingService;
import hhplus.tdd.concert.app.domain.waiting.event.WaitingExpiredEventListener;
import hhplus.tdd.concert.app.domain.waiting.entity.ActiveToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;


@Component
@RequiredArgsConstructor
public class WaitingExpiredEventListenerImpl implements WaitingExpiredEventListener {

    private final WaitingService waitingService;

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    @Override
    public void handleWaitingExpiredEvent(ActiveToken activeToken) { // 전달 . domain 감! application 이나 도메인
        waitingService.deleteActiveToken(activeToken);
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    @Override
    public void handleWaitingExpiredTimeEvent(String value) {
        waitingService.updateActiveToken(value);
    }

}