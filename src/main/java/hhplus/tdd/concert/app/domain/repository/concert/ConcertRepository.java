package hhplus.tdd.concert.app.domain.repository.concert;

import hhplus.tdd.concert.app.domain.entity.concert.Concert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConcertRepository extends JpaRepository<Concert, Long> {
}
