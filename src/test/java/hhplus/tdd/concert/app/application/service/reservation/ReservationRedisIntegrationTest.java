package hhplus.tdd.concert.app.application.service.reservation;

import hhplus.tdd.concert.app.application.reservation.aop.ReserveDistributedLockAop;
import hhplus.tdd.concert.app.application.service.TestBase;
import hhplus.tdd.concert.app.domain.concert.repository.ConcertSeatRepository;
import hhplus.tdd.concert.app.domain.waiting.entity.Member;
import hhplus.tdd.concert.app.domain.waiting.repository.MemberRepository;
import hhplus.tdd.concert.app.domain.reservation.entity.Reservation;
import hhplus.tdd.concert.app.domain.reservation.repository.ReservationRepository;
import hhplus.tdd.concert.app.domain.waiting.entity.Waiting;
import hhplus.tdd.concert.app.domain.waiting.repository.WaitingRepository;
import hhplus.tdd.concert.app.infrastructure.DatabaseCleaner;
import hhplus.tdd.concert.config.types.ReserveStatus;
import hhplus.tdd.concert.config.types.WaitingStatus;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@SpringBootTest
public class ReservationRedisIntegrationTest {

    private final TestBase testBase = new TestBase();

    @Autowired
    private ConcertSeatRepository concertSeatRepository;

    @Autowired
    private WaitingRepository waitingRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private DatabaseCleaner databaseCleaner;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ReserveDistributedLockAop reserveDistributedLockAop;

    @AfterEach
    public void setUp() {
        databaseCleaner.clear();
    }

    @Test
    public void 레디스_PUB_SUB_멤버100명이_1개좌석_쟁탈결과_1명만성공() throws InterruptedException {
        long beforeTime = System.currentTimeMillis();

        // given
        int totalTasks = 100;
        int capacity = 1;
        AtomicInteger failCnt = new AtomicInteger();
        List<Member> memberList = new ArrayList<>();
        List<Waiting> waitingList = new ArrayList<>();
        IntStream.range(0, totalTasks).forEach(i -> {
            Member member = Member.builder().memberName("김소리" + i).charge(15000).build();
            memberList.add(member);
            waitingList.add(Waiting.builder().token("sample-token" + i).member(member).status(WaitingStatus.ACTIVE).createAt(testBase.now.minusMinutes(30)).expiredAt(testBase.now.plusMinutes(30)).build());
        });
        memberRepository.saveAll(memberList);

        IntStream.range(0, totalTasks).forEach(i -> {
            waitingRepository.addActiveToken(testBase.ACTIVE_TOKEN_KEY, testBase.waitingToken + i);
        });

        concertSeatRepository.save(testBase.concertSeatStandBy);

        CountDownLatch latch = new CountDownLatch(totalTasks);
        ExecutorService executorService = Executors.newFixedThreadPool(totalTasks);

        // when
        for (Waiting waiting : waitingList) {
            executorService.execute(() -> {
                try {
                    reserveDistributedLockAop.processReserveRedisPubSub(waiting.getToken(), testBase.concertSeatStandBy.getSeatId());
                } catch (Exception e) {
                    failCnt.getAndIncrement();
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        List<Reservation> reservation = reservationRepository.findAll();

        // then
        assertEquals(totalTasks - capacity, failCnt.get());
        assertEquals(1, reservation.size());
        assertEquals(ReserveStatus.PENDING, reservation.get(0).getReserveStatus());

        executorService.shutdown();

        long afterTime = System.currentTimeMillis();
        double  secDiffTime = (afterTime - beforeTime)/1000.0;

        log.info("[레디스_PUB_SUB_멤버100명이_1개좌석_쟁탈결과_1명만성공] 소요시간: {}s", secDiffTime);
    }


}
