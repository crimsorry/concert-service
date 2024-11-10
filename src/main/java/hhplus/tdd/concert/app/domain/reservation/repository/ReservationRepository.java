package hhplus.tdd.concert.app.domain.reservation.repository;

import hhplus.tdd.concert.app.domain.reservation.entity.Reservation;
import hhplus.tdd.concert.app.domain.waiting.entity.Member;

import java.util.List;

public interface ReservationRepository {

    Reservation save(Reservation reservation);

    List<Reservation> findAll();

    Reservation findByReserveId(Long reserveId);

    List<Reservation> findByMember(Member member);

}
