package hhplus.tdd.concert.app.application.service.waiting;

import hhplus.tdd.concert.app.application.service.TestBase;
import hhplus.tdd.concert.app.application.waiting.service.WaitingService;
import hhplus.tdd.concert.app.domain.concert.entity.ConcertSeat;
import hhplus.tdd.concert.app.domain.concert.repository.ConcertSeatRepository;
import hhplus.tdd.concert.app.domain.member.repository.MemberRepository;
import hhplus.tdd.concert.app.domain.payment.repository.PaymentRepository;
import hhplus.tdd.concert.app.domain.reservation.entity.Reservation;
import hhplus.tdd.concert.app.domain.reservation.repository.ReservationRepository;
import hhplus.tdd.concert.app.domain.waiting.model.ActiveToken;
import hhplus.tdd.concert.app.domain.waiting.repository.WaitingRepository;
import hhplus.tdd.concert.app.infrastructure.DatabaseCleaner;
import hhplus.tdd.concert.config.types.ReserveStatus;
import hhplus.tdd.concert.config.types.SeatStatus;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@SpringBootTest
public class WaitingServiceIntegrationTest {

    private final TestBase testBase = new TestBase();

    @Autowired
    private WaitingService waitingService;

    @Autowired
    private WaitingRepository waitingRepository;

    @Autowired
    private ConcertSeatRepository concertSeatRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private DatabaseCleaner databaseCleaner;

    private static final int TOTAL_REQUESTS = 50000; // 총 요청 수
    private static final int CONCURRENT_THREADS = 100; // 동시 실행 스레드 수

    @AfterEach
    public void setUp() {
        databaseCleaner.clear();
    }

    @Test
    @Transactional
    public void 대기열_11명_active_10명까지_active() {
        // given
        int totalMembers = 11;
        int maxMember = 10;

        for (int i = 0; i < totalMembers; i++) {
            waitingRepository.addWaitingToken(testBase.WAITING_TOKEN_KEY, testBase.waitingToken + i + ":" + i, System.currentTimeMillis());
        }

        // when
        waitingService.activeWaiting();
        List<ActiveToken> waitingTokenList = waitingRepository.getWaitingToken(testBase.WAITING_TOKEN_KEY);
        List<ActiveToken> activeTokenList = waitingRepository.getActiveToken(testBase.ACTIVE_TOKEN_KEY);

        // 결과 검증
        assertEquals(maxMember, activeTokenList.size()); // 10명 ACTIVE
        assertEquals(totalMembers - maxMember, waitingTokenList.size()); // 나머지 1명 STAND_BY
    }

    @Test
    @Transactional
    public void 대기열_만료_확인() {
        // given
        memberRepository.save(testBase.member);
//        waitingRepository.save(testBase.waitingExpired);
        concertSeatRepository.save(testBase.concertSeatReserve);
        reservationRepository.save(testBase.reservationReserve);
        paymentRepository.save(testBase.payment);

        // when
        waitingService.expiredWaiting();

        // then
        Reservation updatedReservation = reservationRepository.findByReserveId(testBase.reservationReserve.getReserveId());
        assertEquals(ReserveStatus.CANCELED, updatedReservation.getReserveStatus());
        ConcertSeat updateConcertSeat = concertSeatRepository.findBySeatId(testBase.concertSeatReserve.getSeatId());
        assertEquals(SeatStatus.STAND_BY, updateConcertSeat.getSeatStatus());
//        Waiting updatedWaiting = waitingRepository.findByWaitingId(testBase.waitingExpired.getWaitingId());
//        assertEquals(WaitingStatus.EXPIRED, updatedWaiting.getStatus());
    }

    @Test
    public void 대기열_순번_동시접속_5만건시_초당_트래픽() {
        memberRepository.save(testBase.member);
//        waitingRepository.save(testBase.waiting);

        ExecutorService executor = Executors.newFixedThreadPool(CONCURRENT_THREADS);
        List<CompletableFuture<Void>> futures = new ArrayList<>();

        Instant start = Instant.now();

        for (int i = 0; i < TOTAL_REQUESTS; i++) {
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                waitingService.loadWaiting(testBase.waitingToken);
            }, executor);
            futures.add(future);
        }

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        Instant end = Instant.now();
        Duration duration = Duration.between(start, end);

        double tps = (TOTAL_REQUESTS * 1000.0) / duration.toMillis(); // 밀리초 기준으로 TPS 계산
        log.info("[대기열_순번_동시접속_5만건시_초당_트래픽] : {}", tps);

        executor.shutdown();
    }



}
