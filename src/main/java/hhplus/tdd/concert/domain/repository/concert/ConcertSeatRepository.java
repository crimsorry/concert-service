package hhplus.tdd.concert.domain.repository.concert;

import hhplus.tdd.concert.domain.entity.concert.ConcertSchedule;
import hhplus.tdd.concert.domain.entity.concert.ConcertSeat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConcertSeatRepository extends JpaRepository<ConcertSeat, Long> {

    ConcertSeat findBySeatId(Long seatId);
    List<ConcertSeat> findBySchedule(ConcertSchedule concertSchedule);

}
