package hhplus.tdd.concert.app.infrastructure.event.waiting;

import hhplus.tdd.concert.app.domain.waiting.event.WaitingExpiredEventListener;
import hhplus.tdd.concert.app.domain.waiting.entity.ActiveToken;
import hhplus.tdd.concert.app.domain.waiting.repository.WaitingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;


@Component
@RequiredArgsConstructor
public class WaitingExpiredEventListenerImpl implements WaitingExpiredEventListener {

    private WaitingRepository waitingRepository;

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    @Override
    public void handleWaitingExpiredEvent(ActiveToken activeToken) {
        waitingRepository.deleteActiveToken("waitingToken", activeToken.getToken() + ":" + activeToken.getMemberId() + ":" + activeToken.getExpiredAt());
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    @Override
    public void handleWaitingExpiredTimeEvent(String value) {
        waitingRepository.updateActiveToken(value);
    }
}