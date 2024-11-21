package hhplus.tdd.concert.app.infrastructure.concert.persistence.implement;

import hhplus.tdd.concert.app.domain.concert.entity.ConcertSchedule;
import hhplus.tdd.concert.app.domain.concert.repository.ConcertScheduleRepository;
import hhplus.tdd.concert.app.infrastructure.concert.persistence.dataacess.jpa.ConcertScheduleJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ConcertScheduleRepositoryImpl implements ConcertScheduleRepository {

    private final ConcertScheduleJpaRepository repository;

    @Override
    public ConcertSchedule save(ConcertSchedule concertSchedule) {
        return repository.save(concertSchedule);
    }

    @Override
    public ConcertSchedule findByScheduleId(Long scheduleId) {
        return repository.findByScheduleId(scheduleId);
    }

    @Override
    public List<ConcertSchedule> findByConcertScheduleDatesWithStandBySeats(Long concertId, Pageable pageable) {
        return repository.findByConcertScheduleDatesWithStandBySeats(concertId, pageable);
    }
}
