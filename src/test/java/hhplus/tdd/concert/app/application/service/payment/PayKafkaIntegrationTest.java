package hhplus.tdd.concert.app.application.service.payment;

import hhplus.tdd.concert.app.application.openapi.dto.KakaoMsgDto;
import hhplus.tdd.concert.app.application.openapi.service.OpenapiService;
import hhplus.tdd.concert.app.application.payment.service.PayService;
import hhplus.tdd.concert.app.application.reservation.service.ReservationService;
import hhplus.tdd.concert.app.application.service.TestBase;
import hhplus.tdd.concert.app.domain.concert.repository.ConcertRepository;
import hhplus.tdd.concert.app.domain.concert.repository.ConcertScheduleRepository;
import hhplus.tdd.concert.app.domain.concert.repository.ConcertSeatRepository;
import hhplus.tdd.concert.app.domain.openapi.event.KakaoMsgEvent;
import hhplus.tdd.concert.app.domain.payment.repository.PaymentRepository;
import hhplus.tdd.concert.app.domain.reservation.repository.ReservationRepository;
import hhplus.tdd.concert.app.domain.waiting.entity.Member;
import hhplus.tdd.concert.app.domain.waiting.repository.MemberRepository;
import hhplus.tdd.concert.app.domain.waiting.repository.WaitingRepository;
import hhplus.tdd.concert.config.KafkaRedisMySqlContainerSupport;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.concurrent.TimeUnit;

import static java.time.Duration.ofSeconds;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Slf4j
@ActiveProfiles("test")
@Testcontainers
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ExtendWith({
        MockitoExtension.class
})
public class PayKafkaIntegrationTest extends KafkaRedisMySqlContainerSupport {

    private final TestBase testBase = new TestBase();

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
    private ReservationRepository reservationRepository;

    @Autowired
    private PaymentRepository paymentRepository;


    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @SpyBean
    private OpenapiService openapiService;
    @Autowired
    private PayService payService;

    @Test
    public void 좌석_예약_시_KAFKA_produce한_알림톡_호출_성공_확인() {
        memberRepository.save(Member.builder().memberName("김소리").charge(6600000).build());
        waitingRepository.addActiveToken(testBase.ACTIVE_TOKEN_KEY, testBase.activeTokenValueOnlyToken + ":1");
        concertRepository.save(testBase.concert);
        concertScheduleRepository.save(testBase.concertSchedule);
        concertSeatRepository.save(testBase.concertSeatReserve);
        reservationRepository.save(testBase.reservationReserve);
        paymentRepository.save(testBase.payment);

        payService.processPayOptimisticLock(testBase.activeTokenValueOnlyToken, 1L);

        await()
                .atMost(60, TimeUnit.SECONDS)  // 5초 wait
                .pollInterval(100, TimeUnit.MILLISECONDS)  // 100ms 간격 check
                .untilAsserted(() -> {
                    verify(openapiService, times(1)).processKakaoMsg(any(KakaoMsgDto.class));
                });
    }

    private static final String KAKAO_TOPIC = "kakao-pay-test-topic";
    private KakaoMsgEvent kakaoMsgEvent = new KakaoMsgEvent("[결제완료]", "김소리", "드라큘라", 15000, "A01");
    private KakaoMsgEvent receivedkakaoMsgEvent = new KakaoMsgEvent("[test]", "김소리", "드라큘라", 15000, "A01");

    @Test
    void 알림톡_송신_수신_정상() {
        // when
        kafkaTemplate.send(KAKAO_TOPIC, kakaoMsgEvent);
        log.info("토픽 : {} , 보낸 메시지 : {}", KAKAO_TOPIC, kakaoMsgEvent);

        // then
        await().atMost(ofSeconds(10))
            .untilAsserted(() -> {
                log.info("받은 메시지 : {}",  kakaoMsgEvent);
                assertThat(receivedkakaoMsgEvent.getMsg()).isEqualTo(kakaoMsgEvent.getMsg());
            });
    }

    @KafkaListener(topics = KAKAO_TOPIC, groupId = "test-group")
    void consumeTestMessage(KakaoMsgEvent event) {
        this.receivedkakaoMsgEvent.setMsg(event.getMsg());
    }


}