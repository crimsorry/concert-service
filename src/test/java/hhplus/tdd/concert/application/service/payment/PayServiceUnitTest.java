package hhplus.tdd.concert.application.service.payment;

import hhplus.tdd.concert.application.dto.concert.ReservationDto;
import hhplus.tdd.concert.application.dto.payment.LoadAmountDto;
import hhplus.tdd.concert.application.dto.payment.UpdateChargeDto;
import hhplus.tdd.concert.application.exception.FailException;
import hhplus.tdd.concert.application.service.PayService;
import hhplus.tdd.concert.domain.entity.concert.ReserveStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PayServiceUnitTest {

    @InjectMocks
    private PayService payService;

    @Test
    public void 잔액_충전_성공() {
        // given
        String waitingToken = "testToken";
        int amountToCharge = 500;

        // when
        UpdateChargeDto result = payService.chargeAmount(waitingToken, amountToCharge);

        // then
        assertTrue(result.isCharge());
    }

    @Test
    public void 잔액_충전_실패_음수_또는_0() {
        // given
        String waitingToken = "testToken";
        int amountToCharge = -500;

        // when & then
        Exception exception = assertThrows(FailException.class, () -> {
            payService.chargeAmount(waitingToken, amountToCharge);
        });

        // 결과 검증
        assertEquals("충전 금액이 0 이하입니다.", exception.getMessage());
    }

    @Test
    public void 잔액_충전_실패_500만_포인트_초과(){
        // given
        String waitingToken = "testToken";
        int amountToCharge = 6000000;

        // when & then
        Exception exception = assertThrows(FailException.class, () -> {
            payService.chargeAmount(waitingToken, amountToCharge);
        });

        // 결과 검증
        assertEquals("충전 한도 초과입니다.", exception.getMessage());
    }

    @Test
    public void 잔액_조회() {
        // given
        String waitingToken = "testToken";

        // when
        LoadAmountDto result = payService.loadAmount(waitingToken);

        // then
        assertEquals(300, result.amount());
    }

    @Test
    public void 결제_처리_성공() {
        // given
        String waitingToken = "testToken";
        long payId = 1L;

        // when
        List<ReservationDto> result = payService.processPay(waitingToken, payId);

        // then
        assertEquals(1, result.size());
        assertEquals(ReserveStatus.RESERVED, result.get(0).reserveStatus());
    }

}