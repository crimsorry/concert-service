package hhplus.tdd.concert.app.domain.payment.repository;

import hhplus.tdd.concert.app.domain.payment.entity.AmountHistory;

import java.util.List;

public interface AmountHistoryRepository {

    List<AmountHistory> findAll();

    AmountHistory save(AmountHistory amountHistory);

    AmountHistory findByPointId(Long amountHistoryId);

}
