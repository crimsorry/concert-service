package hhplus.tdd.concert.domain.entity.payment;

import hhplus.tdd.concert.domain.entity.member.Member;
import hhplus.tdd.concert.domain.exception.ErrorCode;
import hhplus.tdd.concert.domain.exception.FailException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AmountHistoryUnitTest {

    @Test
    public void 유저_포인트_내역_생성(){
        // given
        int amount = 200;
        PointType pointType = PointType.CHARGE;
        Member member = new Member(1L, "김소리", 0);

        // when & then
        AmountHistory amountHistory = AmountHistory.generateAmountHistory(amount, pointType, member);

        // 결과 검증
        assertEquals(amountHistory.getMember(), member);
    }

    @Test
    public void 잔액_양수_아님(){
        // given
        int amount = -200;

        // when & then
        Exception exception = assertThrows(FailException.class, () -> {
            AmountHistory.checkAmountMinus(amount);
        });

        // 결과 검증
        assertEquals(ErrorCode.AMNIOTIC_PAY.getMessage(), exception.getMessage());
    }


}
