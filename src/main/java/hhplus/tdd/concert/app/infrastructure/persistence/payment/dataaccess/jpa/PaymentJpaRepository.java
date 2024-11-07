package hhplus.tdd.concert.app.infrastructure.persistence.payment.dataaccess.jpa;

import hhplus.tdd.concert.app.domain.waiting.entity.Member;
import hhplus.tdd.concert.app.domain.payment.entity.Payment;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

public interface PaymentJpaRepository extends JpaRepository<Payment, Long> {

    Payment findByPayId(Long paymentId);

    @Lock(LockModeType.OPTIMISTIC)
    @Query("select p from Payment p where p.payId = :paymentId")
    Payment findByPayIdOptimisticLock(Long paymentId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select p from Payment p where p.payId = :paymentId")
    Payment findByPayIdWithPessimisticLock(Long paymentId);

    Payment findByMember(Member member);

}
