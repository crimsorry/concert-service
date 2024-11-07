//package hhplus.tdd.concert.app.application.schedule;
//
//import hhplus.tdd.concert.app.application.service.TestBase;
//import hhplus.tdd.concert.app.application.waiting.schedule.WaitingSchedule;
//import hhplus.tdd.concert.app.application.waiting.service.WaitingService;
//import hhplus.tdd.concert.app.domain.concert.repository.ConcertSeatRepository;
//import hhplus.tdd.concert.app.domain.member.repository.MemberRepository;
//import hhplus.tdd.concert.app.domain.payment.repository.PaymentRepository;
//import hhplus.tdd.concert.app.domain.reservation.repository.ReservationRepository;
//import hhplus.tdd.concert.app.domain.waiting.repository.WaitingRepository;
//import hhplus.tdd.concert.app.infrastructure.DatabaseCleaner;
//import lombok.extern.slf4j.Slf4j;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.concurrent.CountDownLatch;
//
//@Slf4j
//@SpringBootTest
//public class WaitingScheduleIntegrationTest {
//
//    private final TestBase testBase = new TestBase();
//
//    @Autowired
//    private WaitingSchedule waitingSchedule;
//
//    @Autowired
//    private WaitingRepository waitingRepository;
//
//    @Autowired
//    private ConcertSeatRepository concertSeatRepository;
//
//    @Autowired
//    private PaymentRepository paymentRepository;
//
//    @Autowired
//    private ReservationRepository reservationRepository;
//
//    @Autowired
//    private MemberRepository memberRepository;
//
//    @Autowired
//    private DatabaseCleaner databaseCleaner;
//
//    private static final int TOTAL_REQUESTS = 50000; // 총 요청 수
//    private static final int CONCURRENT_THREADS = 100; // 동시 실행 스레드 수
//
//    @AfterEach
//    public void setUp() {
//        databaseCleaner.clear();
//    }
//
//    @Test
//    public void 대기열_10초에_6000명_active_전환_1분후_36000명_전환완료(){
//        CountDownLatch latch = new CountDownLatch(6);
//
//        // 스케줄링된 메서드에서 호출되는 메서드에 카운트를 추가하여 횟수를 셉니다.
//        new Thread(() -> {
//            while (latch.getCount() > 0) {
//                // 메서드가 실행될 때마다 카운트를 감소시킴
//                waitingSchedule.activeWaitingSchedule();
//                latch.countDown();
//                try {
//                    Thread.sleep(10000); // 10초 간격으로 실행
//                } catch (InterruptedException e) {
//                    Thread.currentThread().interrupt();
//                    break;
//                }
//            }
//        }).start();
//
//        // 1분(60초) 내에 6번 실행되었는지 확인
//        latch.await(1, TimeUnit.MINUTES);
//    }
//
//
//}
