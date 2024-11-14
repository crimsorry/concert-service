package hhplus.tdd.concert.app.domain.event;

import hhplus.tdd.concert.app.domain.waiting.entity.ActiveToken;
import org.springframework.context.ApplicationEvent;
import lombok.Getter;

@Getter
public class WaitingExpiredEvent extends ApplicationEvent {

    private final ActiveToken activeToken;

    public WaitingExpiredEvent(Object source, ActiveToken activeToken) {
        super(source);
        this.activeToken = activeToken;
    }
}