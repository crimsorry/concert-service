package hhplus.tdd.concert.app.application.service.waiting;

import hhplus.tdd.concert.app.domain.entity.concert.ConcertSeat;
import hhplus.tdd.concert.app.domain.entity.reservation.Reservation;
import hhplus.tdd.concert.app.domain.entity.member.Member;
import hhplus.tdd.concert.app.domain.entity.waiting.Waiting;
import hhplus.tdd.concert.app.domain.repository.concert.ConcertSeatRepository;
import hhplus.tdd.concert.app.domain.repository.concert.ReservationRepository;
import hhplus.tdd.concert.app.domain.repository.member.MemberRepository;
import hhplus.tdd.concert.app.domain.repository.payment.PaymentRepository;
import hhplus.tdd.concert.app.domain.repository.waiting.WaitingRepository;
import hhplus.tdd.concert.app.application.service.TestBase;
import hhplus.tdd.concert.app.infrastructure.DatabaseCleaner;
import hhplus.tdd.concert.common.types.ReserveStatus;
import hhplus.tdd.concert.common.types.SeatStatus;
import hhplus.tdd.concert.common.types.WaitingStatus;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

    @BeforeEach
    public void setUp() {
        databaseCleaner.execute();
    }

    @Test
    @Transactional
    public void 대기열_11명_active_10명까지_active() {
        // given
        int totalMembers = 11;
        int maxMember = 10;

        for (int i = 0; i < totalMembers; i++) {
            Waiting waiting = new Waiting(1L + i, new Member(1L + i, "김소리" + i, 0), "waitingTokenTest" + i , WaitingStatus.STAND_BY, LocalDateTime.now(), LocalDateTime.now().plusMinutes(30));
            waitingRepository.save(waiting);
        }

        // when
        waitingService.activeWaiting();

        // then
        List<Waiting> activeWaitings = waitingRepository.findByStatusOrderByWaitingId(WaitingStatus.ACTIVE, PageRequest.of(0, maxMember));
        List<Waiting> standByWaitings = waitingRepository.findByStatusOrderByWaitingId(WaitingStatus.STAND_BY, PageRequest.of(0, totalMembers));

        // 결과 검증
        assertEquals(maxMember, activeWaitings.size()); // 10명 ACTIVE
        assertEquals(totalMembers - maxMember, standByWaitings.size()); // 나머지 1명 STAND_BY
    }

    @Test
    @Transactional
    public void 대기열_만료_확인() {
        // given
        memberRepository.save(testBase.member);
        waitingRepository.save(testBase.waitingExpired);
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
        Waiting updatedWaiting = waitingRepository.findByWaitingId(testBase.waitingExpired.getWaitingId());
        assertEquals(WaitingStatus.EXPIRED, updatedWaiting.getStatus());
    }

}
