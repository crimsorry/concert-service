package hhplus.tdd.concert.app.domain.event;

import hhplus.tdd.concert.app.domain.waiting.entity.ActiveToken;

public interface WaitingPublisher {

    void publishWaitingExpiredEvent(ActiveToken activeToken);
    void publishWaitingExpiredTimeEvent(String value);

}