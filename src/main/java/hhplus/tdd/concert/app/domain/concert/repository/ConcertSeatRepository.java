package hhplus.tdd.concert.app.domain.concert.repository;

import hhplus.tdd.concert.app.domain.concert.entity.ConcertSchedule;
import hhplus.tdd.concert.app.domain.concert.entity.ConcertSeat;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ConcertSeatRepository {

    ConcertSeat save(ConcertSeat concertSeat);

    ConcertSeat findBySeatId(Long seatId);

    @Lock(LockModeType.OPTIMISTIC)
    @Query("select cs from ConcertSeat cs where cs.seatId = :seatId")
    ConcertSeat findBySeatIdWithOptimisticLock(Long seatId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select cs from ConcertSeat cs where cs.seatId = :seatId")
    ConcertSeat findBySeatIdWithPessimisticLock(Long seatId);

    List<ConcertSeat> findBySchedule(ConcertSchedule concertSchedule);

}
