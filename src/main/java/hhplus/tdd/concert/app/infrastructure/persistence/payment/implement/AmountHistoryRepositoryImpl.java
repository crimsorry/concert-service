package hhplus.tdd.concert.app.infrastructure.persistence.payment.implement;

import hhplus.tdd.concert.app.domain.payment.entity.AmountHistory;
import hhplus.tdd.concert.app.domain.payment.repository.AmountHistoryRepository;
import hhplus.tdd.concert.app.infrastructure.persistence.payment.dataaccess.jpa.AmountHistoryJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class AmountHistoryRepositoryImpl implements AmountHistoryRepository {

    private final AmountHistoryJpaRepository repository;

    @Override
    public List<AmountHistory> findAll() {
        return repository.findAll();
    }

    @Override
    public AmountHistory save(AmountHistory amountHistory) {
        return repository.save(amountHistory);
    }

    @Override
    public AmountHistory findByPointId(Long amountHistoryId) {
        return repository.findByPointId(amountHistoryId);
    }
}
