package hhplus.tdd.concert.app.application.service.payment;

import hhplus.tdd.concert.app.application.payment.service.PayService;
import hhplus.tdd.concert.app.application.service.TestBase;
import hhplus.tdd.concert.app.domain.concert.entity.ConcertSeat;
import hhplus.tdd.concert.app.domain.concert.repository.ConcertRepository;
import hhplus.tdd.concert.app.domain.concert.repository.ConcertScheduleRepository;
import hhplus.tdd.concert.app.domain.member.entity.Member;
import hhplus.tdd.concert.app.domain.concert.repository.ConcertSeatRepository;
import hhplus.tdd.concert.app.domain.payment.entity.AmountHistory;
import hhplus.tdd.concert.app.domain.payment.entity.Payment;
import hhplus.tdd.concert.app.domain.reservation.entity.Reservation;
import hhplus.tdd.concert.app.domain.reservation.repository.ReservationRepository;
import hhplus.tdd.concert.app.domain.member.repository.MemberRepository;
import hhplus.tdd.concert.app.domain.payment.repository.AmountHistoryRepository;
import hhplus.tdd.concert.app.domain.payment.repository.PaymentRepository;
import hhplus.tdd.concert.app.domain.waiting.entity.Waiting;
import hhplus.tdd.concert.app.domain.waiting.repository.WaitingRepository;
import hhplus.tdd.concert.app.infrastructure.DatabaseCleaner;
import hhplus.tdd.concert.common.types.PointType;
import hhplus.tdd.concert.common.types.ReserveStatus;
import hhplus.tdd.concert.common.types.SeatStatus;
import hhplus.tdd.concert.common.types.WaitingStatus;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@SpringBootTest
public class PayServiceIntegrationTest {

    private final TestBase testBase = new TestBase();

    @Autowired
    private PayService payService;

    @Autowired
    private WaitingRepository waitingRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private DatabaseCleaner databaseCleaner;
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private ConcertRepository concertRepository;
    @Autowired
    private ConcertScheduleRepository concertScheduleRepository;
    @Autowired
    private ConcertSeatRepository concertSeatRepository;
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private AmountHistoryRepository amountHistoryRepository;

    @AfterEach
    public void setUp() {
        databaseCleaner.clear();
    }

    @Test
    public void 비관적락_한_유저가_1원_2원_3원_충전시_총_6원_반영() throws InterruptedException {
        long beforeTime = System.currentTimeMillis();

        int totalTasks = 3;
        AtomicInteger chargeAmount = new AtomicInteger(1);
        int totalCharge = testBase.member.getCharge() + chargeAmount.get() + chargeAmount.get()*2 + chargeAmount.get()*3;
        AtomicInteger failCnt = new AtomicInteger();
        memberRepository.save(testBase.member);
        waitingRepository.save(testBase.waitingActive);

        CountDownLatch latch = new CountDownLatch(totalTasks);
        ExecutorService executorService = Executors.newFixedThreadPool(totalTasks);

        IntStream.range(0, totalTasks).forEach(i ->
                executorService.execute(() -> {
                    try {
                        payService.chargeAmount(testBase.waitingActive.getToken(), chargeAmount.getAndIncrement());
                    } catch (ObjectOptimisticLockingFailureException e) {
                        failCnt.getAndIncrement();
                    } finally {
                        latch.countDown();
                    }
                })
        );

        latch.await();

        Member member = memberRepository.findByMemberId(testBase.member.getMemberId());

        assertEquals(totalCharge, member.getCharge());

        long afterTime = System.currentTimeMillis();
        double  secDiffTime = (afterTime - beforeTime)/1000.0;
        log.info("[비관적락_한_유저가_1원_2원_3원_충전시_총_6원_반영] 소요시간: {}s", secDiffTime);
    }

    @Test
    public void 낙관적락_throw_한_유저가_1원_2원_3원_충전시_총_1원_반영() throws InterruptedException {
        long beforeTime = System.currentTimeMillis();

        int totalTasks = 3;
        int chargeAmount = 1;
        int totalCharge = testBase.member.getCharge() + chargeAmount;
        AtomicInteger failCnt = new AtomicInteger();
        memberRepository.save(testBase.member);
        waitingRepository.save(testBase.waitingActive);

        CountDownLatch latch = new CountDownLatch(totalTasks);
        ExecutorService executorService = Executors.newFixedThreadPool(totalTasks);

        IntStream.range(0, totalTasks).forEach(i ->
                executorService.execute(() -> {
                    try {
                        payService.chargeAmountOptimisticLock(testBase.waitingActive.getToken(), chargeAmount);
                    } catch (RuntimeException e) {
                        failCnt.getAndIncrement();
                    } finally {
                        latch.countDown();
                    }
                })
        );

        latch.await();

        Member member = memberRepository.findByMemberId(testBase.member.getMemberId());

        assertEquals(totalTasks-1, failCnt.get());
        assertEquals(totalCharge, member.getCharge());

        long afterTime = System.currentTimeMillis();
        double secDiffTime = (afterTime - beforeTime)/1000.0;

        log.info("[낙관적락_throw_한_유저가_1원_2원_3원_충전시_총_1원_반영] 소요시간: {}s", secDiffTime);
    }

    @Test
    public void 낙관적락_retry_한_유저가_1원_2원_3원_충전시_총_6원_반영() throws InterruptedException {
        long beforeTime = System.currentTimeMillis();

        int totalTasks = 3;
        AtomicInteger chargeAmount = new AtomicInteger(1);
        int totalCharge = testBase.member.getCharge() + chargeAmount.get() + chargeAmount.get() *2 + chargeAmount.get() *3;
        AtomicInteger failCnt = new AtomicInteger();
        memberRepository.save(testBase.member);
        waitingRepository.save(testBase.waitingActive);

        CountDownLatch latch = new CountDownLatch(totalTasks);
        ExecutorService executorService = Executors.newFixedThreadPool(totalTasks);

        IntStream.range(0, totalTasks).forEach(i ->
            executorService.execute(() -> {
                try {
                    payService.chargeAmountOptimisticLockRetry(testBase.waitingActive.getToken(), chargeAmount.getAndIncrement());
                } catch (ObjectOptimisticLockingFailureException e) {
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

        log.info("[낙관적락_retry_한_유저가_1원_3건_충전시_총_3원_반영] 소요시간: {}s", secDiffTime);
    }

    @Test
    public void 비관적락_한_유저가_1건예약_100번결제시_1건결제성공() throws InterruptedException {
        long beforeTime = System.currentTimeMillis();

        int totalTasks = 100;
        AtomicInteger failCnt = new AtomicInteger();
        memberRepository.save(testBase.member);
        waitingRepository.save(testBase.waitingActive);
        paymentRepository.save(testBase.payment);
        concertSeatRepository.save(testBase.concertSeatReserve);
        reservationRepository.save(testBase.reservationReserve);

        CountDownLatch latch = new CountDownLatch(totalTasks);
        ExecutorService executorService = Executors.newFixedThreadPool(totalTasks);

        IntStream.range(0, totalTasks).forEach(i ->
                executorService.execute(() -> {
                    try {
                        payService.processPay(testBase.waitingActive.getToken(), testBase.payment.getPayId());
                    } catch (Exception e) {
                        failCnt.getAndIncrement();
                    }finally {
                        latch.countDown();
                    }
                })
        );

        latch.await();

        Member member = memberRepository.findByMemberId(testBase.member.getMemberId());
        Waiting waiting = waitingRepository.findByWaitingId(testBase.waitingActive.getWaitingId());
        Payment payment = paymentRepository.findByPayId(testBase.payment.getPayId());
        Reservation reservation = reservationRepository.findByReserveId(testBase.reservationReserve.getReserveId());
        ConcertSeat concertSeat = concertSeatRepository.findBySeatId(testBase.concertSeatReserve.getSeatId());
        AmountHistory amountHistory = amountHistoryRepository.findByPointId(1L);

        assertEquals(totalTasks - 1, failCnt.get());
        assertEquals(true, payment.getIsPay());
        assertEquals(ReserveStatus.RESERVED, reservation.getReserveStatus());
        assertEquals(SeatStatus.ASSIGN, concertSeat.getSeatStatus());
        assertEquals(WaitingStatus.EXPIRED, waiting.getStatus());
        assertEquals(testBase.payment.getAmount(), amountHistory.getAmount());
        assertEquals(PointType.USE, amountHistory.getPointType());
        assertEquals(testBase.member.getCharge() - testBase.payment.getAmount(), member.getCharge());

        long afterTime = System.currentTimeMillis();
        double  secDiffTime = (afterTime - beforeTime)/1000.0;
        log.info("[비관적락_한_유저가_1건예약_100번결제시_1건결제성공] 소요시간: {}s", secDiffTime);
    }

    @Test
    public void 낙관락_throw_한_유저가_1건예약_100번결제시_1건결제성공() throws InterruptedException {
        long beforeTime = System.currentTimeMillis();

        int totalTasks = 100;
        AtomicInteger failCnt = new AtomicInteger();
        memberRepository.save(testBase.member);
        waitingRepository.save(testBase.waitingActive);
        paymentRepository.save(testBase.payment);
        concertSeatRepository.save(testBase.concertSeatReserve);
        reservationRepository.save(testBase.reservationReserve);

        CountDownLatch latch = new CountDownLatch(totalTasks);
        ExecutorService executorService = Executors.newFixedThreadPool(totalTasks);

        IntStream.range(0, totalTasks).forEach(i ->
                executorService.execute(() -> {
                    try {
                        payService.processPayOptimisticLock(testBase.waitingActive.getToken(), testBase.payment.getPayId());
                    } catch (RuntimeException e) {
                        failCnt.getAndIncrement();
                    } finally {
                        latch.countDown();
                    }
                })
        );

        latch.await();

        Member member = memberRepository.findByMemberId(testBase.member.getMemberId());
        Waiting waiting = waitingRepository.findByWaitingId(testBase.waitingActive.getWaitingId());
        Payment payment = paymentRepository.findByPayId(testBase.payment.getPayId());
        Reservation reservation = reservationRepository.findByReserveId(testBase.reservationReserve.getReserveId());
        ConcertSeat concertSeat = concertSeatRepository.findBySeatId(testBase.concertSeatReserve.getSeatId());
        List<AmountHistory> amountHistorys = amountHistoryRepository.findAll();

        log.error("에러다~~ {}", amountHistorys.get(0).getPointId());

        assertEquals(totalTasks - 1, failCnt.get());
        assertEquals(true, payment.getIsPay());
        assertEquals(ReserveStatus.RESERVED, reservation.getReserveStatus());
        assertEquals(SeatStatus.ASSIGN, concertSeat.getSeatStatus());
        assertEquals(WaitingStatus.EXPIRED, waiting.getStatus());
        assertEquals(1, amountHistorys.size());
        assertEquals(testBase.payment.getAmount(), amountHistorys.get(0).getAmount());
        assertEquals(PointType.USE, amountHistorys.get(0).getPointType());
        assertEquals(testBase.member.getCharge() - testBase.payment.getAmount(), member.getCharge());

        long afterTime = System.currentTimeMillis();
        double  secDiffTime = (afterTime - beforeTime)/1000.0;
        log.info("[낙관락_throw_한_유저가_1건예약_100번결제시_1건결제성공] 소요시간: {}s", secDiffTime);
    }


}
