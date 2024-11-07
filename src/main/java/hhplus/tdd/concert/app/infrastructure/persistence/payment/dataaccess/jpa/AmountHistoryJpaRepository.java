package hhplus.tdd.concert.app.infrastructure.persistence.payment.dataaccess.jpa;

import hhplus.tdd.concert.app.domain.payment.entity.AmountHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface AmountHistoryJpaRepository extends JpaRepository<AmountHistory, Long> {

    AmountHistory findByPointId(Long amountHistoryId);

}
