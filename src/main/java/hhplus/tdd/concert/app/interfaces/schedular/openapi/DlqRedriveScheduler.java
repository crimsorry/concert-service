package hhplus.tdd.concert.app.interfaces.schedular.openapi;

//import com.fasterxml.jackson.databind.ObjectMapper;
//import hhplus.tdd.concert.app.domain.waiting.event.WaitingExpiredEvent;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.kafka.clients.consumer.ConsumerRecord;
//import org.apache.kafka.clients.consumer.ConsumerRecords;
//import org.apache.kafka.clients.consumer.KafkaConsumer;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import java.time.Duration;
//import java.util.Collections;
//import java.util.concurrent.BlockingQueue;
//import java.util.concurrent.LinkedBlockingQueue;
//
//@Component
//@RequiredArgsConstructor
//@Slf4j
//public class RetryScheduler {
//    private final KafkaTemplate<String, Object> kafkaTemplate;
//    private final KafkaConsumer<String, Object> kafkaConsumer;
//
//    @Scheduled(fixedDelay = 30000) // 5분마다 실행
//    public void processFailedMessages() {
////        consumer.subscribe(Collections.singletonList("waiting_expired_event.DLQ"));
////        ConsumerRecords<String, Object> records = consumer.poll(Duration.ofSeconds(1));
//
//        kafkaConsumer.subscribe(Collections.singletonList("waiting_event.DLQ"));
//        ConsumerRecords<String, Object> records =
//                kafkaConsumer.poll(Duration.ofSeconds(1));
//
//        for (ConsumerRecord<String, Object> record : records) {
//            try {
//                kafkaTemplate.send("waiting_event", record.value());
//                log.info("메시지 재처리 성공: {}", record.value());
//            } catch (Exception e) {
//                log.error("메시지 재처리 실패: {}", record.value(), e);
//            }
//        }
//    }
//
//
//}

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.apache.kafka.common.header.internals.RecordHeaders;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Collections;

@Slf4j
@Component
public class DlqRedriveScheduler {

    private static final String RETRY_COUNT_HEADER = "retry-count";
    private static final int MAX_RETRY_COUNT = 5;

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final KafkaConsumer<String, Object> kafkaConsumer;

    public DlqRedriveScheduler(KafkaTemplate<String, Object> kafkaTemplate, KafkaConsumer<String, Object> kafkaConsumer) {
        this.kafkaTemplate = kafkaTemplate;
        this.kafkaConsumer = kafkaConsumer;
    }

    @Scheduled(fixedDelay = 30000) // 30초마다 실행
    public void processDlqMessages() {
        // DLQ 토픽에서 메시지 읽기
        kafkaConsumer.subscribe(Collections.singletonList("kakao-event.DLQ"));
        ConsumerRecords<String, Object> records = kafkaConsumer.poll(Duration.ofSeconds(5));

        for (ConsumerRecord<String, Object> record : records) {
            int retryCount = getRetryCount(record);

            if (retryCount >= MAX_RETRY_COUNT) {
                kafkaTemplate.send("kakao-event.permanent_fail", record.value());
                log.warn("최대 재시도 초과. 영구 삭제 토픽으로 이동: {}", record.value());
                continue;
            }

            try {
                // 재시도
                ProducerRecord<String, Object> producerRecord = new ProducerRecord<>(
                        "kakao-event",
                        null,
                        null,
                        record.key(),
                        record.value(),
                        incrementRetryCount(record.headers(), retryCount)
                );
                kafkaTemplate.send(producerRecord);
                log.info("DLQ 메시지 재발행 성공. Retry Count: {}, Value: {}", retryCount + 1, record.value());
            } catch (Exception e) {
                log.error("DLQ 메시지 재발행 실패. Retry Count: {}, Value: {}", retryCount, record.value(), e);
            }
        }
    }

    private int getRetryCount(ConsumerRecord<String, Object> record) {
        Header retryHeader = record.headers().lastHeader(RETRY_COUNT_HEADER);
        if (retryHeader != null) {
            return Integer.parseInt(new String(retryHeader.value(), StandardCharsets.UTF_8));
        }
        return 0;
    }

    private Iterable<Header> incrementRetryCount(Iterable<Header> headers, int currentRetryCount) {
        RecordHeaders updatedHeaders = new RecordHeaders();
        headers.forEach(header -> {
            if (!header.key().equals(RETRY_COUNT_HEADER)) {
                updatedHeaders.add(header);
            }
        });
        updatedHeaders.add(new RecordHeader(RETRY_COUNT_HEADER, String.valueOf(currentRetryCount + 1).getBytes(StandardCharsets.UTF_8)));
        return updatedHeaders;
    }
}