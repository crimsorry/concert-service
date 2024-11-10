package hhplus.tdd.concert.app.infrastructure.persistence.concert.dataaccess.jpa;

import hhplus.tdd.concert.app.domain.concert.entity.ConcertSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

public interface ConcertScheduleJpaRepository extends JpaRepository<ConcertSchedule, Long> {

    ConcertSchedule findByScheduleId(Long scheduleId);

    @Query("SELECT DISTINCT cs FROM ConcertSchedule cs " +
            "JOIN ConcertSeat seat ON seat.schedule = cs " +
            "WHERE cs.startDate <= :now " +
            "AND cs.endDate >= :now " +
            "AND seat.seatStatus = hhplus.tdd.concert.config.types.SeatStatus.STAND_BY")
    List<ConcertSchedule> findByConcertScheduleDatesWithStandBySeats(LocalDateTime now);

}