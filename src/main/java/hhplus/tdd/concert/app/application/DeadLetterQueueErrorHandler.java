package hhplus.tdd.concert.app.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ConsumerAwareListenerErrorHandler;
import org.springframework.kafka.listener.ListenerExecutionFailedException;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DeadLetterQueueErrorHandler implements ConsumerAwareListenerErrorHandler {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public Object handleError(Message<?> message, ListenerExecutionFailedException exception, Consumer<?, ?> consumer) {
        log.error("[waiting] 메시지 처리 실패, DLQ로 이동: {}", exception.getMessage());
        kafkaTemplate.send("waiting_event.DLQ", message.getPayload());
        return null;
    }
}
