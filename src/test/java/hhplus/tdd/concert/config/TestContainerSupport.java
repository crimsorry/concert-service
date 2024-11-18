package hhplus.tdd.concert.config;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.wait.strategy.Wait; // 추가한 내용
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.kafka.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@Slf4j
@SpringBootTest(webEnvironment = RANDOM_PORT)
@Testcontainers
public abstract class TestContainerSupport {

    static JdbcDatabaseContainer MYSQL = new MySQLContainer("mysql:8");

    static GenericContainer REDIS = new GenericContainer("redis:6.2.6");

    static KafkaContainer KAFKA = new KafkaContainer(
            DockerImageName.parse("confluentinc/cp-kafka:7.5.0") // 원하는 Kafka 버전
                    .asCompatibleSubstituteFor("apache/kafka")
    );

    @BeforeAll
    static void beforeAll() {
        log.info("start======================");
        MYSQL.start();
        REDIS.start();
        KAFKA.start();
    }

    @AfterAll
    static void AfterAll() {
        MYSQL.stop();
        REDIS.stop();
        KAFKA.stop();
        log.info("stop======================");
    }

    @DynamicPropertySource
    public static void overrideProps(DynamicPropertyRegistry registry) {
        // mysql
        registry.add("spring.datasource.driver-class-name", MYSQL::getDriverClassName);
        registry.add("spring.datasource.url", MYSQL::getJdbcUrl);
        registry.add("spring.datasource.~username", MYSQL::getUsername);
        registry.add("spring.datasource.password", MYSQL::getPassword);

        // redis
        registry.add("spring.data.redis.host", REDIS::getHost);
        registry.add("spring.data.redis.port", () -> REDIS.getMappedPort(6379));

        // kafka
        registry.add("spring.kafka.bootstrap-servers", KAFKA::getBootstrapServers);
    }

    static {
        // 추가한 내용: 각 컨테이너에 waiting 전략 추가
        MYSQL.waitingFor(Wait.forListeningPort());
        REDIS.waitingFor(Wait.forListeningPort());
        KAFKA.waitingFor(Wait.forListeningPort());
    }
}

