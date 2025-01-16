package hhplus.tdd.concert.app.domain.payment.repository;

import hhplus.tdd.concert.app.domain.payment.entity.Payment;
import hhplus.tdd.concert.app.domain.waiting.entity.Member;

public interface PaymentRepository  {

    Payment save(Payment payment);

    Payment findByPayId(Long paymentId);

    Payment findByPayIdOptimisticLock(Long paymentId);

    Payment findByPayIdWithPessimisticLock(Long paymentId);

    Payment findByMember(Member member);

}
