package hhplus.tdd.concert.application.service.user;

import hhplus.tdd.concert.application.exception.FailException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserServiceUnitTest {

    @InjectMocks
    private UserService userService;

    @Test
    public void 잔액_충전_성공() {
        // given
        String queueToken = "testToken";
        int amountToCharge = 500;

        // when
        boolean result = userService.chargeAmount(queueToken, amountToCharge);

        // then
        assertTrue(result);
    }

    @Test
    public void 잔액_충전_실패_음수_또는_0() {
        // given
        String queueToken = "testToken";
        int amountToCharge = -500;

        // when & then
        Exception exception = assertThrows(FailException.class, () -> {
            userService.chargeAmount(queueToken, amountToCharge);
        });

        // 결과 검증
        assertEquals("충전 금액이 0 이하입니다.", exception.getMessage());
    }

    @Test
    public void 잔액_충전_실패_500만_포인트_초과(){
        // given
        String queueToken = "testToken";
        int amountToCharge = 6000000;

        // when & then
        Exception exception = assertThrows(FailException.class, () -> {
            userService.chargeAmount(queueToken, amountToCharge);
        });

        // 결과 검증
        assertEquals("충전 한도 초과입니다.", exception.getMessage());
    }

    @Test
    public void 잔액_조회() {
        // given
        String queueToken = "testToken";

        // when
        int result = userService.amount(queueToken);

        // then
        assertEquals(300, result);
    }

}