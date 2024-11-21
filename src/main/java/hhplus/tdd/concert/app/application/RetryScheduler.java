package hhplus.tdd.concert.app.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import hhplus.tdd.concert.app.domain.waiting.event.WaitingExpiredEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Collections;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Component
@RequiredArgsConstructor
@Slf4j
public class RetryScheduler {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final KafkaConsumer<String, Object> kafkaConsumer;

    @Scheduled(fixedDelay = 30000) // 5분마다 실행
    public void processFailedMessages() {
//        consumer.subscribe(Collections.singletonList("waiting_expired_event.DLQ"));
//        ConsumerRecords<String, Object> records = consumer.poll(Duration.ofSeconds(1));

        kafkaConsumer.subscribe(Collections.singletonList("waiting_event.DLQ"));
        ConsumerRecords<String, Object> records =
                kafkaConsumer.poll(Duration.ofSeconds(1));

        for (ConsumerRecord<String, Object> record : records) {
            try {
                kafkaTemplate.send("waiting_event", record.value());
                log.info("메시지 재처리 성공: {}", record.value());
            } catch (Exception e) {
                log.error("메시지 재처리 실패: {}", record.value(), e);
            }
        }
    }


}