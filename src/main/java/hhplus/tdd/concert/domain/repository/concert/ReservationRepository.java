package hhplus.tdd.concert.domain.repository.concert;

import hhplus.tdd.concert.domain.entity.concert.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    Reservation findByReserveId(Long reserveId);

}
