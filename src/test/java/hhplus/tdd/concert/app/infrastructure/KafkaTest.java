package hhplus.tdd.concert.app.infrastructure;

import hhplus.tdd.concert.config.KafkaRedisMySqlContainerSupport;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@Slf4j
@ActiveProfiles("test")
@Testcontainers
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class KafkaTest extends KafkaRedisMySqlContainerSupport {

    private static final String TEST_TOPIC = "test-topic";

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private String receivedMessage;

    @Test
    void Kafka_produce_토픽_broker_전달_후_consumer_받은_메세지_일치_확인() {
        String message = "kafka connect ok!";

        kafkaTemplate.send(TEST_TOPIC, message);
        log.info("토픽 : {} , 보낸 메시지 : {}",TEST_TOPIC, message);
        await()
                .atMost(Duration.ofSeconds(10))
                .untilAsserted(() -> {
                    log.info("받은 메시지 : {}",  receivedMessage);
                    assertThat(receivedMessage).isEqualTo(message);
                });
    }

    @KafkaListener(topics = TEST_TOPIC, groupId = "test-group")
    public void consumeTestMessage(String message) {
        this.receivedMessage = message;
    }

}