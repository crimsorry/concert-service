package hhplus.tdd.concert.app.domain.repository.concert;

import hhplus.tdd.concert.app.domain.entity.concert.Reservation;
import hhplus.tdd.concert.app.domain.entity.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    Reservation findByReserveId(Long reserveId);
    List<Reservation> findByMember(Member member);

}
