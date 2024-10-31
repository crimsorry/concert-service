package hhplus.tdd.concert.app.domain.payment.repository;

import hhplus.tdd.concert.app.domain.payment.entity.AmountHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AmountHistoryRepository extends JpaRepository<AmountHistory, Long> {

    AmountHistory findByPointId(Long amountHistoryId);

}
