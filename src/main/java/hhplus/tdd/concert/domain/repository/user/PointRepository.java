package hhplus.tdd.concert.domain.repository.user;

import hhplus.tdd.concert.domain.entity.user.AmountHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PointRepository extends JpaRepository<AmountHistory, Long> {
}
