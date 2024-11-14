package hhplus.tdd.concert.app.application.service;

import hhplus.tdd.concert.app.domain.concert.entity.Concert;
import hhplus.tdd.concert.app.domain.concert.entity.ConcertSchedule;
import hhplus.tdd.concert.app.domain.concert.entity.ConcertSeat;
import hhplus.tdd.concert.app.domain.waiting.entity.Member;
import hhplus.tdd.concert.app.domain.payment.entity.Payment;
import hhplus.tdd.concert.app.domain.reservation.entity.Reservation;
import hhplus.tdd.concert.app.domain.waiting.entity.Waiting;
import hhplus.tdd.concert.app.domain.waiting.entity.ActiveToken;
import hhplus.tdd.concert.config.types.ReserveStatus;
import hhplus.tdd.concert.config.types.SeatStatus;
import hhplus.tdd.concert.config.types.WaitingStatus;

import java.time.LocalDateTime;
import java.util.List;

public class TestBase {

    // given
    public final String WAITING_TOKEN_KEY = "waitingToken";
    public final String ACTIVE_TOKEN_KEY = "activeToken";
    public final int amount = 500;
    public final String title = "드라큘라";
    public final String waitingToken = "testToken";
    public final String waitingToken2 = "testToken2";
    public final String activeTokenValueOnlyToken = "testActiveToken";
    public final String activeTokenValue = "testActiveToken:1:" + System.currentTimeMillis();
    public final LocalDateTime now = LocalDateTime.now();
    public final Member member = Member.builder().memberId(1L).memberName("김소리").charge(9000000).build();
    public final Member member2 = Member.builder().memberId(2L).memberName("김소리").charge(16000).build();
    public final Waiting waiting = new Waiting(1L, member, waitingToken, WaitingStatus.STAND_BY, now, now.plusMinutes(30));
    public final Waiting waitingActive = new Waiting(1L, member, waitingToken, WaitingStatus.ACTIVE, now, now.plusMinutes(30));
    public final Waiting waitingActive2 = new Waiting(2L, member2, waitingToken2, WaitingStatus.ACTIVE, now, now.plusMinutes(30));
    public final Waiting waitingExpired = new Waiting(1L, member, waitingToken, WaitingStatus.ACTIVE, now.minusMinutes(30), now.minusMinutes(10));
    public final ActiveToken waitToken = new ActiveToken(WAITING_TOKEN_KEY, 1L, 0L);
    public final ActiveToken activeToken = new ActiveToken(ACTIVE_TOKEN_KEY, 1L, System.currentTimeMillis());
    public final Concert concert = new Concert(1L, title, "부산문화회관 대극장");
    public final ConcertSchedule concertSchedule = new ConcertSchedule(1L, concert, now, now.minusDays(1), now.plusDays(1));
    public final ConcertSeat concertSeatReserve = new ConcertSeat(1L, concertSchedule, "A01", 140000, SeatStatus.RESERVED);
    public final ConcertSeat concertSeatStandBy = new ConcertSeat(2L, concertSchedule, "A01", 140000, SeatStatus.STAND_BY);
    public final Reservation reservationReserve = new Reservation(1L, member, concertSeatReserve, "드라큘라", now, "A01", 140000, ReserveStatus.PENDING);
    public final Payment payment = new Payment(1L, member, reservationReserve, 140000, false, now, 0);
    public final List<Concert> concerts = List.of(concert);
    public final List<ConcertSchedule> concertSchedules = List.of(concertSchedule);
    public final List<ConcertSeat> concertSeats = List.of(concertSeatStandBy);
    public final List<Reservation> reservations = List.of(reservationReserve);

}
