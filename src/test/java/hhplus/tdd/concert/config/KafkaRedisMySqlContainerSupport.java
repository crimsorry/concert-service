package hhplus.tdd.concert.config;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.*;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@Slf4j
@SpringBootTest(webEnvironment = RANDOM_PORT)
public abstract class KafkaRedisMySqlContainerSupport {

    // Kafka Testcontainer 설정
    private static final KafkaContainer KAFKA = new KafkaContainer(
            DockerImageName.parse("confluentinc/cp-kafka:7.5.0")
                    .asCompatibleSubstituteFor("apache/kafka") // Testcontainers에서 Kafka 설정을 호환
    );

    static final GenericContainer<?> REDIS = new GenericContainer<>("redis:latest")
            .waitingFor(Wait.forListeningPort())
            .withExposedPorts(6379);

    static JdbcDatabaseContainer MYSQL = (JdbcDatabaseContainer) new MySQLContainer("mysql:8")
            .waitingFor(Wait.forListeningPort());

    @BeforeAll
    public static void startContainers() {
        log.info("Starting containers...");
        KAFKA.start();
        REDIS.start();
        MYSQL.start();
        log.info("Containers started successfully.");
    }

    @AfterAll
    public static void stopContainers() {
        log.info("Stopping containers...");
        KAFKA.stop();
        REDIS.stop();
        MYSQL.stop();
        log.info("Containers stopped successfully.");
    }

    @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry registry) {
        // Kafka 설정 주입
        registry.add("spring.kafka.bootstrap-servers", KAFKA::getBootstrapServers);

        registry.add("spring.data.redis.host", REDIS::getHost);
        registry.add("spring.data.redis.port", () -> REDIS.getMappedPort(6379));

        registry.add("spring.datasource.driver-class-name", MYSQL::getDriverClassName);
        registry.add("spring.datasource.url", MYSQL::getJdbcUrl);
        registry.add("spring.datasource.username", MYSQL::getUsername);
        registry.add("spring.datasource.password", MYSQL::getPassword);

    }

    // Kafka 상태 확인 메서드
    public static boolean isKafkaRunning() {
        return KAFKA.isRunning();
    }

    public static boolean isRedisRunning() {
        return REDIS.isRunning();
    }

    public static boolean isMYSQLRunning() {
        return MYSQL.isRunning();
    }
}