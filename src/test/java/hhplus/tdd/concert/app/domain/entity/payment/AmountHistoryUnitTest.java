package hhplus.tdd.concert.app.domain.entity.payment;

import hhplus.tdd.concert.app.domain.member.entity.Member;
import hhplus.tdd.concert.app.domain.payment.entity.AmountHistory;
import hhplus.tdd.concert.config.types.PointType;
import hhplus.tdd.concert.app.domain.exception.ErrorCode;
import hhplus.tdd.concert.config.exception.FailException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AmountHistoryUnitTest {

    private final Member member = Member.builder()
            .memberName("김소리")
            .charge(0)
            .build();

    @Test
    public void 포인트_내역_빌더() {
        // given
        int amount = 500;
        PointType pointType = PointType.CHARGE;

        // when & then
        AmountHistory amountHistory = AmountHistory.generateAmountHistory(amount, pointType, member);

        // 결과 검증
        assertEquals(member, amountHistory.getMember());
        assertEquals(amount, amountHistory.getAmount());
    }

    @Test
    public void 유저_포인트_내역_생성(){
        // given
        int amount = 200;
        PointType pointType = PointType.CHARGE;

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
            AmountHistory.checkAmountMinusOrZero(amount);
        });

        // 결과 검증
        assertEquals(ErrorCode.AMNIOTIC_PAY.getMessage(), exception.getMessage());
    }


}
