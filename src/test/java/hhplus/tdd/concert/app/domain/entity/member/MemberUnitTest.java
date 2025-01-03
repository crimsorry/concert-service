package hhplus.tdd.concert.app.domain.entity.member;

import hhplus.tdd.concert.app.domain.exception.ErrorCode;
import hhplus.tdd.concert.app.domain.waiting.entity.Member;
import hhplus.tdd.concert.config.exception.FailException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MemberUnitTest {

    @Test
    public void 존재_하지_않는_맴버(){
        // given
        Member member = null;

        // when & then
        Exception exception = assertThrows(FailException.class, () -> {
            Member.checkMemberExistence(member);
        });

        // 결과 검증
        assertEquals(ErrorCode.NOT_FOUNT_MEMBER.getMessage(), exception.getMessage());
    }

    @Test
    public void 한도_초과(){
        // given
        int amount = 6000000;
        Member member = Member.builder()
                .charge(5000000)
                .build();

        // when & then
        Exception exception = assertThrows(FailException.class, () -> {
            Member.checkMemberCharge(member, amount);
        });

        // 결과 검증
        assertEquals(ErrorCode.FULL_PAY.getMessage(), exception.getMessage());
    }

    @Test
    public void 잔액_부족(){
        // given
        int amount = 4000;
        Member member = Member.builder()
                .charge(300)
                .build();

        // when & then
        Exception exception = assertThrows(FailException.class, () -> {
            Member.checkMemberChargeLess(member, amount);
        });

        // 결과 검증
        assertEquals(ErrorCode.EMPTY_PAY.getMessage(), exception.getMessage());
    }

    @Test
    public void 유저_잔액_충전(){
        // given
        Member member = Member.builder()
                .charge(500)
                .build();

        // when & then
        member.charge(20);

        // 결과 검증
        assertEquals(520, member.getCharge());
    }

    @Test
    public void 유저_잔액_차감(){
        // given
        Member member = Member.builder()
                .charge(500)
                .build();

        // when & then
        member.withdraw(20);

        // 결과 검증
        assertEquals(480, member.getCharge());
    }

}
