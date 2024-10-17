package hhplus.tdd.concert.domain.entity.payment;

import hhplus.tdd.concert.domain.exception.ErrorCode;
import hhplus.tdd.concert.domain.exception.FailException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PaymentUnitTest {

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
        Payment payment = new Payment();
        payment.setIsPay(true);

        // when & then
        Exception exception = assertThrows(FailException.class, () -> {
            Payment.checkPaymentStatue(payment);
        });

        // 결과 검증
        assertEquals(ErrorCode.SUCCESS_PAY_SEAT.getMessage(), exception.getMessage());
    }

}
