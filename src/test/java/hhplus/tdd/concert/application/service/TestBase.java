package hhplus.tdd.concert.application.service;

import hhplus.tdd.concert.domain.entity.concert.*;
import hhplus.tdd.concert.domain.entity.member.Member;
import hhplus.tdd.concert.domain.entity.payment.AmountHistory;
import hhplus.tdd.concert.domain.entity.payment.Payment;
import hhplus.tdd.concert.domain.entity.payment.PointType;
import hhplus.tdd.concert.domain.entity.waiting.Waiting;
import hhplus.tdd.concert.domain.entity.waiting.WaitingStatus;

import java.time.LocalDateTime;
import java.util.List;

public abstract class TestBase {

    // given
    protected final int amount = 500;
    protected final String title = "드라큘라";
    protected final String waitingToken = "testToken";
    protected final String waitingToken2 = "testToken2";
    protected final LocalDateTime now = LocalDateTime.now();
    protected final Member member = new Member(35L, "김소리", 150000);
    protected final Member member2 = new Member(22L, "김소리2", 160000);
    protected final Waiting waiting = new Waiting(1L, member, waitingToken, WaitingStatus.STAND_BY, now, now.plusMinutes(30));
    protected final Waiting waitingActive = new Waiting(22L, member, waitingToken, WaitingStatus.ACTIVE, now, now.plusMinutes(30));
    protected final Waiting waitingActive2 = new Waiting(23L, member2, waitingToken2, WaitingStatus.ACTIVE, now, now.plusMinutes(30));
    protected final Waiting waitingExpired = new Waiting(91L, member, waitingToken, WaitingStatus.ACTIVE, now.minusMinutes(30), now.minusMinutes(10));
    protected final Concert concert = new Concert(1L, title, "부산문화회관 대극장");
    protected final ConcertSchedule concertSchedule = new ConcertSchedule(1L, concert, now, now.minusDays(1), now.plusDays(1), 50);
    protected final ConcertSeat concertSeatReserve = new ConcertSeat(105L, concertSchedule, "F01", 140000, SeatStatus.RESERVED);
    protected final ConcertSeat concertSeatStandBy = new ConcertSeat(106L, concertSchedule, "F01", 140000, SeatStatus.STAND_BY);
    protected final Reservation reservationReserve = new Reservation(18L, member, concertSeatReserve, "드라큘라", now, "A01", 140000, ReserveStatus.PENDING);
    protected final Payment payment = new Payment(18L, member, reservationReserve, 140000, false, now);
    protected final List<ConcertSchedule> concertSchedules = List.of(concertSchedule);
    protected final List<ConcertSeat> concertSeats = List.of(concertSeatStandBy);

}
