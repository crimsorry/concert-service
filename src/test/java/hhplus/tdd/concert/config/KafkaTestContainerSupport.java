package hhplus.tdd.concert.config;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.*;
import org.testcontainers.utility.DockerImageName;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@Slf4j
@SpringBootTest(webEnvironment = RANDOM_PORT)
public abstract class TestContainerSupport {

    // Kafka Testcontainer 설정
    private static final KafkaContainer KAFKA = new KafkaContainer(
            DockerImageName.parse("confluentinc/cp-kafka:7.5.0")
                    .asCompatibleSubstituteFor("apache/kafka") // Testcontainers에서 Kafka 설정을 호환
    );

    @BeforeAll
    public static void startContainers() {
        log.info("Starting containers...");
        KAFKA.start();
        log.info("Containers started successfully.");
    }

    @AfterAll
    public static void stopContainers() {
        log.info("Stopping containers...");
        KAFKA.stop();
        log.info("Containers stopped successfully.");
    }

    @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry registry) {
        // Kafka 설정 주입
        registry.add("spring.kafka.bootstrap-servers", KAFKA::getBootstrapServers);
    }

    // Kafka 상태 확인 메서드
    public static boolean isKafkaRunning() {
        return KAFKA.isRunning();
    }
}