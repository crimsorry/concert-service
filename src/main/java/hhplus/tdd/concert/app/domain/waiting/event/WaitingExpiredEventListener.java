package hhplus.tdd.concert.app.domain.waiting.event;

import hhplus.tdd.concert.app.domain.waiting.entity.ActiveToken;

public interface WaitingExpiredEventListener {
    void handleWaitingExpiredEvent(WaitingExpiredEvent waitingExpiredEvent);
}