package hhplus.tdd.concert.app.domain.repository.concert;

import hhplus.tdd.concert.app.domain.entity.concert.ConcertSchedule;
import hhplus.tdd.concert.app.domain.entity.concert.ConcertSeat;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConcertSeatRepository extends JpaRepository<ConcertSeat, Long> {

    ConcertSeat findBySeatId(Long seatId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select cs from ConcertSeat cs where cs.seatId = :seatId")
    ConcertSeat findBySeatIdWithPessimisticLock(Long seatId);

    List<ConcertSeat> findBySchedule(ConcertSchedule concertSchedule);

}
