package hhplus.tdd.concert.app.domain.waiting.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class WaitingExpiredTimeEvent {

    private String value;

}