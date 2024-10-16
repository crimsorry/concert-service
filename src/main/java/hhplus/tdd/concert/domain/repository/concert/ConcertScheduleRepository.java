package hhplus.tdd.concert.domain.repository.concert;

import hhplus.tdd.concert.domain.entity.concert.ConcertSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ConcertScheduleRepository extends JpaRepository<ConcertSchedule, Long> {

    ConcertSchedule findByScheduleId(Long scheduleId);
    @Query("SELECT cs FROM ConcertSchedule cs WHERE cs.startDate > :now AND cs.endDate <= :now AND cs.capacity > :capacity")
    List<ConcertSchedule> findByConcertScheduleDates(LocalDateTime now, int capacity);

}
