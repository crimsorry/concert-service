package hhplus.tdd.concert.app.application.service.reservation;

import hhplus.tdd.concert.app.domain.entity.member.Member;
import hhplus.tdd.concert.app.domain.entity.reservation.Reservation;
import hhplus.tdd.concert.app.domain.entity.waiting.Waiting;
import hhplus.tdd.concert.app.domain.repository.concert.ConcertSeatRepository;
import hhplus.tdd.concert.app.domain.repository.concert.ReservationRepository;
import hhplus.tdd.concert.app.domain.repository.member.MemberRepository;
import hhplus.tdd.concert.app.domain.repository.waiting.WaitingRepository;
import hhplus.tdd.concert.app.application.service.TestBase;
import hhplus.tdd.concert.app.infrastructure.DatabaseCleaner;
import hhplus.tdd.concert.common.types.ReserveStatus;
import hhplus.tdd.concert.common.types.WaitingStatus;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
public class ReservationServiceIntegrationTest {

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
    private DatabaseCleaner databaseCleaner;
    @Autowired
    private ReservationRepository reservationRepository;

    @AfterEach
    public void setUp() {
        databaseCleaner.clear();
    }
}