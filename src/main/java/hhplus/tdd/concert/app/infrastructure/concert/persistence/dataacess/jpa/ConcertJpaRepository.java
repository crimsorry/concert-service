package hhplus.tdd.concert.app.infrastructure.concert.persistence.dataacess.jpa;

import hhplus.tdd.concert.app.domain.concert.entity.Concert;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConcertJpaRepository extends JpaRepository<Concert, Long> {

}
