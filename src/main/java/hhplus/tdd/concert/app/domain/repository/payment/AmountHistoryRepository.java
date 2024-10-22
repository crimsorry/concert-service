package hhplus.tdd.concert.app.domain.repository.payment;

import hhplus.tdd.concert.app.domain.entity.payment.AmountHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AmountHistoryRepository extends JpaRepository<AmountHistory, Long> {



}
