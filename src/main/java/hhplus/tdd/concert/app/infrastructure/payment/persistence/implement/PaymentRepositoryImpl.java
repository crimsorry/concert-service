package hhplus.tdd.concert.app.infrastructure.payment.persistence.implement;

import hhplus.tdd.concert.app.domain.payment.entity.Payment;
import hhplus.tdd.concert.app.domain.payment.repository.PaymentRepository;
import hhplus.tdd.concert.app.domain.waiting.entity.Member;
import hhplus.tdd.concert.app.infrastructure.payment.persistence.dataaccess.jpa.PaymentJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PaymentRepositoryImpl implements PaymentRepository {

    private final PaymentJpaRepository repository;

    @Override
    public Payment save(Payment payment) {
        return repository.save(payment);
    }

    @Override
    public Payment findByPayId(Long paymentId) {
        return repository.findByPayId(paymentId);
    }

    @Override
    public Payment findByPayIdOptimisticLock(Long paymentId) {
        return repository.findByPayIdOptimisticLock(paymentId);
    }

    @Override
    public Payment findByPayIdWithPessimisticLock(Long paymentId) {
        return repository.findByPayIdWithPessimisticLock(paymentId);
    }

    @Override
    public Payment findByMember(Member member) {
        return repository.findByMember(member);
    }
}
