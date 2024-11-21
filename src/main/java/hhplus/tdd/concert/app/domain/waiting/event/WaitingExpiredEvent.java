package hhplus.tdd.concert.app.domain.waiting.event;

import hhplus.tdd.concert.app.domain.waiting.entity.ActiveToken;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.context.ApplicationEvent;
import lombok.Getter;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class WaitingExpiredEvent {

    private ActiveToken activeToken;

}