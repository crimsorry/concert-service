package hhplus.tdd.concert.app.application.service.reservation;

import hhplus.tdd.concert.app.application.DlqRedriveScheduler;
import hhplus.tdd.concert.app.application.reservation.service.ReservationService;
import hhplus.tdd.concert.app.application.service.TestBase;
import hhplus.tdd.concert.app.application.waiting.service.WaitingService;
import hhplus.tdd.concert.app.domain.concert.repository.ConcertRepository;
import hhplus.tdd.concert.app.domain.concert.repository.ConcertScheduleRepository;
import hhplus.tdd.concert.app.domain.concert.repository.ConcertSeatRepository;
import hhplus.tdd.concert.app.domain.waiting.entity.ActiveToken;
import hhplus.tdd.concert.app.domain.waiting.entity.Member;
import hhplus.tdd.concert.app.domain.waiting.event.WaitingExpiredTimeEvent;
import hhplus.tdd.concert.app.domain.waiting.event.WaitingPublisher;
import hhplus.tdd.concert.app.domain.waiting.repository.MemberRepository;
import hhplus.tdd.concert.app.domain.waiting.repository.WaitingRepository;
import hhplus.tdd.concert.config.KafkaRedisMySqlContainerSupport;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.apache.kafka.common.header.internals.RecordHeaders;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.org.awaitility.Awaitility;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static java.time.Duration.ofSeconds;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;

@Slf4j
@ActiveProfiles("test")
@Testcontainers
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ReservationKafkaIntegrationTest extends KafkaRedisMySqlContainerSupport {


    private final TestBase testBase = new TestBase();

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ConcertSeatRepository concertSeatRepository;

    @Autowired
    private WaitingRepository waitingRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ConcertScheduleRepository concertScheduleRepository;

    @Autowired
    private ConcertRepository concertRepository;

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private WaitingPublisher waitingPublisher;

    @Test
    public void 좌석_예약_시_KAFKA_produce한_대기열_삭제_성공_확인() {
        memberRepository.save(Member.builder().memberName("김소리").charge(15000).build());
        waitingRepository.addActiveToken(testBase.ACTIVE_TOKEN_KEY, testBase.activeTokenValueOnlyToken + ":1");
        concertRepository.save(testBase.concert);
        concertScheduleRepository.save(testBase.concertSchedule);
        concertSeatRepository.save(testBase.concertSeatStandBy);

        reservationService.processReserve(testBase.activeTokenValueOnlyToken, 1L);

        Awaitility.await()
                .atMost(10, TimeUnit.SECONDS)  // 5초 wait
                .pollInterval(100, TimeUnit.MILLISECONDS)  // 100ms 간격 check
                .until(() -> {
                    return !waitingRepository.isActiveToken(testBase.ACTIVE_TOKEN_KEY, testBase.activeTokenValueOnlyToken + ":1");
                });

        Boolean isWaiting = waitingRepository.isActiveToken(testBase.ACTIVE_TOKEN_KEY, testBase.activeTokenValueOnlyToken + ":1");

        // then
        assertEquals(false, isWaiting);
    }

    private static final String WAITING_TOPIC = "waiting_event";
    private WaitingExpiredTimeEvent receivedWaitingTimeMessage = new WaitingExpiredTimeEvent("test waiting");
    private WaitingExpiredTimeEvent WaitingTimeMessage = new WaitingExpiredTimeEvent("sending waiting update");

    @Test
    void 대기열_송신_수신_정상() {
        // when
        kafkaTemplate.send(WAITING_TOPIC, WaitingTimeMessage);
        log.info("토픽 : {} , 보낸 메시지 : {}", WAITING_TOPIC, WaitingTimeMessage);

        // then
        await().atMost(ofSeconds(10))
            .untilAsserted(() -> {
                log.info("받은 메시지 : {}",  WaitingTimeMessage);
                assertThat(receivedWaitingTimeMessage.getValue()).isEqualTo(WaitingTimeMessage.getValue());
            });
    }

    @KafkaListener(topics = WAITING_TOPIC, groupId = "test-group")
    void consumeTestMessage(WaitingExpiredTimeEvent event) {
        receivedWaitingTimeMessage = event;
    }

    @MockBean
    private WaitingService waitingService;

    @Test
    void 대기열_consume_실패로_retry_집입() {
        // given
        doThrow(new RuntimeException("대기열 진입 실패"))
                .when(waitingService)
                .updateActiveToken(any(String.class));

        // when
        kafkaTemplate.send("waiting_expired_event", WaitingTimeMessage);

        // then
        await().atMost(ofSeconds(10))
                .untilAsserted(() -> {
                    log.info("받은 메시지 : {}",  WaitingTimeMessage);
                    assertThat(receivedWaitingTimeMessage.getValue()).isEqualTo(WaitingTimeMessage.getValue());
                });
    }

    @KafkaListener(topics = "waiting_expired_event.DLQ", groupId = "test-dlp-group")
    void consumeDLQTestMessage(WaitingExpiredTimeEvent event) {
        receivedWaitingTimeMessage = event;
    }


}