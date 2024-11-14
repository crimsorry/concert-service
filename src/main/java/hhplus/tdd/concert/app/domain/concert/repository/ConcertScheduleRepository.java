package hhplus.tdd.concert.app.domain.concert.repository;

import hhplus.tdd.concert.app.domain.concert.entity.ConcertSchedule;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

public interface ConcertScheduleRepository {

    ConcertSchedule save(ConcertSchedule concertSchedule);

    ConcertSchedule findByScheduleId(Long scheduleId);

    List<ConcertSchedule> findByConcertScheduleDatesWithStandBySeats(Long concertId, Pageable pageable);

}
