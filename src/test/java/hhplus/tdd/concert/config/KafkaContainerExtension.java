package hhplus.tdd.concert.config;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.kafka.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;


@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles("test")
public class KafkaContainerExtension implements BeforeAllCallback {

    private static final KafkaContainer KAFKA = new KafkaContainer(
            DockerImageName.parse("confluentinc/cp-kafka:7.5.0") // Kafka 이미지
                    .asCompatibleSubstituteFor("apache/kafka")
    ).waitingFor(Wait.forListeningPort()); // 컨테이너가 포트를 열 때까지 대기

    @DynamicPropertySource
    public static void overrideProps(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.bootstrap-servers", KAFKA::getBootstrapServers);
    }

    @Override
    public void beforeAll(ExtensionContext context) {
        if (!KAFKA.isRunning()) {
            KAFKA.start(); // 테스트 실행 전 Kafka 컨테이너 시작
        }
    }
}