package hhplus.tdd.concert.app.domain.waiting.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class WaitingExpiredTimeEvent extends ApplicationEvent {

    private final String value;

    public WaitingExpiredTimeEvent(Object source, String value) {
        super(source);
        this.value = value;
    }
}