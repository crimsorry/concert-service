package hhplus.tdd.concert.app.application.service.reservation;

import hhplus.tdd.concert.app.application.reservation.aop.ReserveDistributedLockAop;
import hhplus.tdd.concert.app.application.reservation.service.ReservationService;
import hhplus.tdd.concert.app.application.service.TestBase;
import hhplus.tdd.concert.app.domain.concert.repository.ConcertRepository;
import hhplus.tdd.concert.app.domain.concert.repository.ConcertScheduleRepository;
import hhplus.tdd.concert.app.domain.concert.repository.ConcertSeatRepository;
import hhplus.tdd.concert.app.domain.reservation.entity.Reservation;
import hhplus.tdd.concert.app.domain.reservation.repository.ReservationRepository;
import hhplus.tdd.concert.app.domain.waiting.entity.Member;
import hhplus.tdd.concert.app.domain.waiting.repository.MemberRepository;
import hhplus.tdd.concert.app.domain.waiting.repository.WaitingRepository;
import hhplus.tdd.concert.app.infrastructure.DatabaseCleaner;
import hhplus.tdd.concert.config.KafkaRedisMySqlContainerSupport;
import hhplus.tdd.concert.config.types.ReserveStatus;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.org.awaitility.Awaitility;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@ActiveProfiles("test")
@Testcontainers
@SpringBootTest
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

}