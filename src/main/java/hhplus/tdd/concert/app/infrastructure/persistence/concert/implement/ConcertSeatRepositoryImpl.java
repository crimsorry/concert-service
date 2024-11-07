package hhplus.tdd.concert.app.infrastructure.persistence.concert.implement;

import hhplus.tdd.concert.app.domain.concert.entity.ConcertSchedule;
import hhplus.tdd.concert.app.domain.concert.entity.ConcertSeat;
import hhplus.tdd.concert.app.domain.concert.repository.ConcertSeatRepository;
import hhplus.tdd.concert.app.infrastructure.persistence.concert.dataaccess.jpa.ConcertSeatJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ConcertSeatRepositoryImpl implements ConcertSeatRepository {

    private final ConcertSeatJpaRepository repository;

    @Override
    public ConcertSeat save(ConcertSeat concertSeat) {
        return repository.save(concertSeat);
    }

    @Override
    public ConcertSeat findBySeatId(Long seatId) {
        return repository.findBySeatIdWithOptimisticLock(seatId);
    }

    @Override
    public ConcertSeat findBySeatIdWithOptimisticLock(Long seatId) {
        return repository.findBySeatIdWithOptimisticLock(seatId);
    }

    @Override
    public ConcertSeat findBySeatIdWithPessimisticLock(Long seatId) {
        return repository.findBySeatIdWithPessimisticLock(seatId);
    }

    @Override
    public List<ConcertSeat> findBySchedule(ConcertSchedule concertSchedule) {
        return repository.findBySchedule(concertSchedule);
    }
}
