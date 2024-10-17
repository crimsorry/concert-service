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
    protected final LocalDateTime now = LocalDateTime.now();
    protected final Member member = new Member(1L, "김소리", 150000);
    protected final Waiting waiting = new Waiting(1L, member, waitingToken, WaitingStatus.STAND_BY, now, now.plusMinutes(30));
    protected final Concert concert = new Concert(1L, title, "부산문화회관 대극장");
    protected final ConcertSchedule concertSchedule = new ConcertSchedule(1L, concert, now, now.minusDays(1), now.plusDays(1), 50);
    protected final ConcertSeat concertSeatReserve = new ConcertSeat(1L, concertSchedule, "A01", 140000, SeatStatus.RESERVED);
    protected final ConcertSeat concertSeatStandBy = new ConcertSeat(1L, concertSchedule, "A01", 140000, SeatStatus.STAND_BY);
    protected final Reservation reservationReserve = new Reservation(1L, member, concertSeatReserve, "드라큘라", now, "A01", 140000, ReserveStatus.PENDING);
    protected final Payment payment = new Payment(1L, member, reservationReserve, 140000, false, now);
    protected final List<ConcertSchedule> concertSchedules = List.of(concertSchedule);
    protected final List<ConcertSeat> concertSeats = List.of(concertSeatStandBy);

}
