package hhplus.tdd.concert.app.infrastructure;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class TestConsumer {

    private final BlockingQueue<String> messageQueue = new LinkedBlockingQueue<>();

    @KafkaListener(topics = "test-topic", groupId = "test-consumer-group")
    public void listen(ConsumerRecord<String, String> record) {
        log.info("Consumed message: {}", record.value());
        messageQueue.add(record.value());
    }

    public String getLastMessage(long timeout, TimeUnit unit) throws InterruptedException {
        return messageQueue.poll(timeout, unit);
    }

    public void clearMessages() {
        messageQueue.clear();
    }
}