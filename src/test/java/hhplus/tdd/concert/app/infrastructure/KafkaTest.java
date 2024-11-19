package hhplus.tdd.concert.app.infrastructure;

import hhplus.tdd.concert.config.KafkaRedisContainerSupport;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Slf4j
@ActiveProfiles("test")
@Testcontainers
public class KafkaTest extends KafkaRedisContainerSupport {

    @Test
    void Kafka_컨테이너_실행_및_종료_테스트() {
        // given
        log.info("Kafka 컨테이너 실행 테스트 시작");

        // when
        KafkaRedisContainerSupport.startContainers();
        log.info("Kafka 컨테이너 실행 중...");

        // then
        assertThat(isKafkaRunning()).isTrue();
        log.info("Kafka 컨테이너가 성공적으로 실행되었습니다.");

        // 종료 확인
        KafkaRedisContainerSupport.stopContainers();
        log.info("Kafka 컨테이너 종료 완료");
    }
}