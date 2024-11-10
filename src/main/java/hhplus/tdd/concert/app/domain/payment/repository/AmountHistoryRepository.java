package hhplus.tdd.concert.app.domain.payment.repository;

import hhplus.tdd.concert.app.domain.payment.entity.AmountHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface AmountHistoryRepository {

    List<AmountHistory> findAll();

    AmountHistory save(AmountHistory amountHistory);

    AmountHistory findByPointId(Long amountHistoryId);

}
