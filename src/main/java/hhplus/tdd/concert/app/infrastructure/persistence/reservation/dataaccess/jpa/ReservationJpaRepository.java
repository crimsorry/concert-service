package hhplus.tdd.concert.app.infrastructure.persistence.reservation.dataaccess.jpa;

import hhplus.tdd.concert.app.domain.waiting.entity.Member;
import hhplus.tdd.concert.app.domain.reservation.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationJpaRepository extends JpaRepository<Reservation, Long> {

    Reservation findByReserveId(Long reserveId);

    List<Reservation> findByMember(Member member);


}
