package hhplus.tdd.concert.app.application.service;

import hhplus.tdd.concert.app.domain.entity.concert.*;
import hhplus.tdd.concert.app.domain.entity.member.Member;
import hhplus.tdd.concert.app.domain.entity.payment.Payment;
import hhplus.tdd.concert.app.domain.entity.reservation.Reservation;
import hhplus.tdd.concert.app.domain.entity.waiting.Waiting;
import hhplus.tdd.concert.common.types.ReserveStatus;
import hhplus.tdd.concert.common.types.SeatStatus;
import hhplus.tdd.concert.common.types.WaitingStatus;

import java.time.LocalDateTime;
import java.util.List;

public class TestBase {

    // given
    public final int amount = 500;
    public final String title = "드라큘라";
    public final String waitingToken = "testToken";
    public final String waitingToken2 = "testToken2";
    public final LocalDateTime now = LocalDateTime.now();
    public final Member member = new Member(1L, "김소리", 150000);
    public final Member member2 = new Member(2L, "김소리2", 160000);
    public final Waiting waiting = new Waiting(1L, member, waitingToken, WaitingStatus.STAND_BY, now, now.plusMinutes(30));
    public final Waiting waitingActive = new Waiting(1L, member, waitingToken, WaitingStatus.ACTIVE, now, now.plusMinutes(30));
    public final Waiting waitingActive2 = new Waiting(2L, member2, waitingToken2, WaitingStatus.ACTIVE, now, now.plusMinutes(30));
    public final Waiting waitingExpired = new Waiting(3L, member, waitingToken, WaitingStatus.ACTIVE, now.minusMinutes(30), now.minusMinutes(10));
    public final Concert concert = new Concert(1L, title, "부산문화회관 대극장");
    public final ConcertSchedule concertSchedule = new ConcertSchedule(1L, concert, now, now.minusDays(1), now.plusDays(1));
    public final ConcertSeat concertSeatReserve = new ConcertSeat(1L, concertSchedule, "A01", 140000, SeatStatus.RESERVED);
    public final ConcertSeat concertSeatStandBy = new ConcertSeat(2L, concertSchedule, "A01", 140000, SeatStatus.STAND_BY);
    public final Reservation reservationReserve = new Reservation(1L, member, concertSeatReserve, "드라큘라", now, "A01", 140000, ReserveStatus.PENDING);
    public final Payment payment = new Payment(1L, member, reservationReserve, 140000, false, now);
    public final List<ConcertSchedule> concertSchedules = List.of(concertSchedule);
    public final List<ConcertSeat> concertSeats = List.of(concertSeatStandBy);
    public final List<Reservation> reservations = List.of(reservationReserve);

}
