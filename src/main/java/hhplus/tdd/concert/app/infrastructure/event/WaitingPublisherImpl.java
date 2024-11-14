package hhplus.tdd.concert.app.infrastructure.event;

import hhplus.tdd.concert.app.domain.event.WaitingExpiredTimeEvent;
import hhplus.tdd.concert.app.domain.event.WaitingPublisher;
import hhplus.tdd.concert.app.domain.event.WaitingExpiredEvent;
import hhplus.tdd.concert.app.domain.waiting.entity.ActiveToken;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WaitingPublisherImpl implements WaitingPublisher {

    private final ApplicationEventPublisher eventPublisher;

    public void publishWaitingExpiredEvent(ActiveToken activeToken) {
        eventPublisher.publishEvent(new WaitingExpiredEvent(this, activeToken));
    }

    @Override
    public void publishWaitingExpiredTimeEvent(String value) {
        eventPublisher.publishEvent(new WaitingExpiredTimeEvent(this, value));
    }
}