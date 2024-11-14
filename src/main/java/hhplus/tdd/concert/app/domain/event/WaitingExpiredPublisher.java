package hhplus.tdd.concert.app.domain.event;

import hhplus.tdd.concert.app.domain.waiting.entity.ActiveToken;

public interface WaitingExpiredPublisher {

    void publishWaitingExpiredEvent(ActiveToken activeToken);

}