package hhplus.tdd.concert.app.infrastructure.event;

import hhplus.tdd.concert.app.domain.event.WaitingExpiredPublisher;
import hhplus.tdd.concert.app.domain.event.WaitingExpiredEvent;
import hhplus.tdd.concert.app.domain.waiting.entity.ActiveToken;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WaitingExpiredPublisherImpl implements WaitingExpiredPublisher {

    private final ApplicationEventPublisher eventPublisher;

    public void publishWaitingExpiredEvent(ActiveToken activeToken) {
        eventPublisher.publishEvent(new WaitingExpiredEvent(this, activeToken));
    }
}