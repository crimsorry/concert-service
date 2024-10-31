package hhplus.tdd.concert.app.domain.entity.payment;

import hhplus.tdd.concert.app.domain.reservation.entity.Reservation;
import hhplus.tdd.concert.app.domain.payment.entity.Payment;
import hhplus.tdd.concert.common.types.ReserveStatus;
import hhplus.tdd.concert.app.domain.member.entity.Member;
import hhplus.tdd.concert.app.domain.exception.ErrorCode;
import hhplus.tdd.concert.common.config.exception.FailException;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PaymentUnitTest {

    private final Member member = Member.builder().memberId(1L).memberName("김소리").charge(0).build();
    private final Reservation reservation = new Reservation(1L, member, null, "드라큘라", LocalDateTime.now(), "A01", 140000, ReserveStatus.PENDING);

    @Test
    public void 결제_빌더() {
        // when & then
        Payment payment = Payment.generatePayment(member, reservation);

        // 결과 검증
        assertEquals(member, payment.getMember());
        assertEquals(false, payment.getIsPay());
    }

    @Test
    public void 존재_하지_않는_결제(){
        // given
        Payment payment = null;

        // when & then
        Exception exception = assertThrows(FailException.class, () -> {
            Payment.checkPaymentExistence(payment);
        });

        // 결과 검증
        assertEquals(ErrorCode.NOT_FOUNT_PAYMENT.getMessage(), exception.getMessage());
    }

    @Test
    public void 결제_완료_상태(){
        // given
        Payment payment = Payment.builder()
                .isPay(true)
                .build();

        // when & then
        Exception exception = assertThrows(FailException.class, () -> {
            Payment.checkPaymentStatue(payment);
        });

        // 결과 검증
        assertEquals(ErrorCode.SUCCESS_PAY_SEAT.getMessage(), exception.getMessage());
    }

    @Test
    public void 결제_완료_확인(){
        // given
        Payment payment = Payment.builder()
                .isPay(false)
                .build();

        // when & then
        payment.done();

        // 결과 검증
        assertEquals(true, payment.getIsPay());
    }

}
