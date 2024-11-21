package hhplus.tdd.concert.app.infrastructure.waiting.event;

import hhplus.tdd.concert.app.domain.waiting.event.WaitingExpiredTimeEvent;
import hhplus.tdd.concert.app.domain.waiting.event.WaitingPublisher;
import hhplus.tdd.concert.app.domain.waiting.event.WaitingExpiredEvent;
import hhplus.tdd.concert.app.domain.waiting.entity.ActiveToken;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WaitingPublisherImpl implements WaitingPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    private static final String WAITING_EXPIRED_TOPIC = "waiting_expired_event";
    private static final String WAITING_EXPIRED_TIME_TOPIC = "waiting_expired_time_event";


    public void publishWaitingExpiredEvent(ActiveToken activeToken) {
        kafkaTemplate.send(WAITING_EXPIRED_TOPIC, new WaitingExpiredEvent(activeToken));
    }

    @Override
    public void publishWaitingExpiredTimeEvent(String value) {
        kafkaTemplate.send(WAITING_EXPIRED_TIME_TOPIC, new WaitingExpiredTimeEvent(value));
    }
}