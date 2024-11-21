package hhplus.tdd.concert.app.domain.waiting.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class WaitingExpiredTimeEvent {

    private String value;

}