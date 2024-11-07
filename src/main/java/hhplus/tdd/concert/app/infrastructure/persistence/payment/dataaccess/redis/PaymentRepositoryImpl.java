package hhplus.tdd.concert.app.infrastructure.persistence.payment.dataaccess.redis;

import hhplus.tdd.concert.app.domain.waiting.entity.Member;
import hhplus.tdd.concert.app.domain.payment.entity.Payment;
import hhplus.tdd.concert.app.domain.payment.repository.PaymentRepository;
import hhplus.tdd.concert.app.infrastructure.persistence.payment.dataaccess.jpa.PaymentJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PaymentRepositoryImpl implements PaymentRepository {

    private final PaymentJpaRepository paymentJpaRepository;

    @Override
    public Payment save(Payment payment) {
        return paymentJpaRepository.save(payment);
    }

    @Override
    public Payment findByPayId(Long paymentId) {
        return paymentJpaRepository.findByPayId(paymentId);
    }

    @Override
    public Payment findByPayIdOptimisticLock(Long paymentId) {
        return paymentJpaRepository.findByPayIdOptimisticLock(paymentId);
    }

    @Override
    public Payment findByPayIdWithPessimisticLock(Long paymentId) {
        return paymentJpaRepository.findByPayIdWithPessimisticLock(paymentId);
    }

    @Override
    public Payment findByMember(Member member) {
        return paymentJpaRepository.findByMember(member);
    }
}
