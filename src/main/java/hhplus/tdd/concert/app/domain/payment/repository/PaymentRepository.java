package hhplus.tdd.concert.app.domain.payment.repository;

import hhplus.tdd.concert.app.domain.member.entity.Member;
import hhplus.tdd.concert.app.domain.payment.entity.Payment;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Payment findByPayId(Long paymentId);

    @Lock(LockModeType.OPTIMISTIC)
    @Query("select p from Payment p where p.payId = :paymentId")
    Payment findByPayIdOptimisticLock(Long paymentId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select p from Payment p where p.payId = :paymentId")
    Payment findByPayIdWithPessimisticLock(Long paymentId);

    Payment findByMember(Member member);

}
