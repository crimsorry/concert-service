package hhplus.tdd.concert.app.application.service.payment;

import hhplus.tdd.concert.app.application.payment.aop.PayDistributedLockAop;
import hhplus.tdd.concert.app.application.service.TestBase;
import hhplus.tdd.concert.app.domain.concert.entity.ConcertSeat;
import hhplus.tdd.concert.app.domain.concert.repository.ConcertSeatRepository;
import hhplus.tdd.concert.app.domain.member.entity.Member;
import hhplus.tdd.concert.app.domain.member.repository.MemberRepository;
import hhplus.tdd.concert.app.domain.payment.entity.AmountHistory;
import hhplus.tdd.concert.app.domain.payment.entity.Payment;
import hhplus.tdd.concert.app.domain.payment.repository.AmountHistoryRepository;
import hhplus.tdd.concert.app.domain.payment.repository.PaymentRepository;
import hhplus.tdd.concert.app.domain.reservation.entity.Reservation;
import hhplus.tdd.concert.app.domain.reservation.repository.ReservationRepository;
import hhplus.tdd.concert.app.domain.waiting.entity.Waiting;
import hhplus.tdd.concert.app.domain.waiting.repository.WaitingRepository;
import hhplus.tdd.concert.app.infrastructure.DatabaseCleaner;
import hhplus.tdd.concert.app.infrastructure.TestContainerConfig;
import hhplus.tdd.concert.config.types.PointType;
import hhplus.tdd.concert.config.types.ReserveStatus;
import hhplus.tdd.concert.config.types.SeatStatus;
import hhplus.tdd.concert.config.types.WaitingStatus;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@SpringBootTest
@ExtendWith(TestContainerConfig.class)
public class PayRedisIntegrationTest {

    private final TestBase testBase = new TestBase();

    @Autowired
    private WaitingRepository waitingRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private DatabaseCleaner databaseCleaner;
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private ConcertSeatRepository concertSeatRepository;
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private AmountHistoryRepository amountHistoryRepository;
    @Autowired
    private PayDistributedLockAop payDistributedLockAop;

    @AfterEach
    public void setUp() {
        databaseCleaner.clear();
    }

    @Test
    public void 레디스_PUB_SUB_한_유저가_1원_2원_3원_충전시_총_6원_반영() throws InterruptedException {
        long beforeTime = System.currentTimeMillis();

        int totalTasks = 3;
        AtomicInteger chargeAmount = new AtomicInteger(1);
        int totalCharge = testBase.member.getCharge() + chargeAmount.get() + chargeAmount.get() *2 + chargeAmount.get() *3;
        AtomicInteger failCnt = new AtomicInteger();
        memberRepository.save(testBase.member);
        waitingRepository.addActiveToken(testBase.ACTIVE_TOKEN_KEY, testBase.waitingToken);

        CountDownLatch latch = new CountDownLatch(totalTasks);
        ExecutorService executorService = Executors.newFixedThreadPool(totalTasks);

        IntStream.range(0, totalTasks).forEach(i ->
                        executorService.execute(() -> {
                            try {
                                payDistributedLockAop.chargeAmountRedisPubSub(testBase.waitingActive.getToken(), chargeAmount.getAndIncrement());
//                        payService.chargeAmountRedisPubSub(testBase.waitingActive.getToken(), chargeAmount.getAndIncrement());
                            } catch (Exception e) {
                                log.error("에러: {}", e.getMessage());
                                failCnt.getAndIncrement();
                            } finally {
                                latch.countDown();
                            }
                        })
        );

        latch.await();

        Member member = memberRepository.findByMemberId(testBase.member.getMemberId());

        assertEquals(0, failCnt.get());
        assertEquals(totalCharge, member.getCharge());

        long afterTime = System.currentTimeMillis();
        double secDiffTime = (afterTime - beforeTime)/1000.0;

        log.info("[레디스_PUB_SUB_한_유저가_1원_2원_3원_충전시_총_6원_반영] 소요시간: {}s", secDiffTime);
    }



    @Test
    public void 레디스_PUB_SUB_한_유저가_1건예약_100번결제시_1건결제성공() throws InterruptedException {
        long beforeTime = System.currentTimeMillis();

        int totalTasks = 100;
        AtomicInteger failCnt = new AtomicInteger();
        memberRepository.save(testBase.member);
        waitingRepository.addActiveToken(testBase.ACTIVE_TOKEN_KEY, testBase.waitingToken);
        paymentRepository.save(testBase.payment);
        concertSeatRepository.save(testBase.concertSeatReserve);
        reservationRepository.save(testBase.reservationReserve);

        CountDownLatch latch = new CountDownLatch(totalTasks);
        ExecutorService executorService = Executors.newFixedThreadPool(totalTasks);

        IntStream.range(0, totalTasks).forEach(i ->
                executorService.execute(() -> {
                    try {
                        payDistributedLockAop.processPayRedisPubSub(testBase.waitingActive.getToken(), testBase.payment.getPayId());
                    } catch (Exception e) {
                        failCnt.getAndIncrement();
                    }finally {
                        latch.countDown();
                    }
                })
        );

        latch.await();

        Member member = memberRepository.findByMemberId(testBase.member.getMemberId());
//        Waiting waiting = waitingRepository.findByWaitingId(testBase.waitingActive.getWaitingId());
        Payment payment = paymentRepository.findByPayId(testBase.payment.getPayId());
        Reservation reservation = reservationRepository.findByReserveId(testBase.reservationReserve.getReserveId());
        ConcertSeat concertSeat = concertSeatRepository.findBySeatId(testBase.concertSeatReserve.getSeatId());
        List<AmountHistory> amountHistorys = amountHistoryRepository.findAll();

        assertEquals(totalTasks - 1, failCnt.get());
        assertEquals(true, payment.getIsPay());
        assertEquals(ReserveStatus.RESERVED, reservation.getReserveStatus());
        assertEquals(SeatStatus.ASSIGN, concertSeat.getSeatStatus());
//        assertEquals(WaitingStatus.EXPIRED, waiting.getStatus());
        assertEquals(1, amountHistorys.size());
        assertEquals(testBase.payment.getAmount(), amountHistorys.get(0).getAmount());
        assertEquals(PointType.USE, amountHistorys.get(0).getPointType());
        assertEquals(testBase.member.getCharge() - testBase.payment.getAmount(), member.getCharge());

        long afterTime = System.currentTimeMillis();
        double  secDiffTime = (afterTime - beforeTime)/1000.0;

        log.info("[레디스_PUB_SUB_한_유저가_1건예약_100번결제시_1건결제성공] 소요시간: {}s", secDiffTime);
    }


}
