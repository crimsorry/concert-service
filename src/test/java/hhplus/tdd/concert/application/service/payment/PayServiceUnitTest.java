package hhplus.tdd.concert.application.service.payment;

import hhplus.tdd.concert.app.application.dto.payment.LoadAmountQuery;
import hhplus.tdd.concert.app.application.dto.payment.UpdateChargeCommand;
import hhplus.tdd.concert.app.application.dto.reservation.ReservationCommand;
import hhplus.tdd.concert.app.domain.repository.waiting.wrapper.WaitingWrapRepository;
import hhplus.tdd.concert.app.application.service.payment.PayService;
import hhplus.tdd.concert.app.domain.entity.payment.AmountHistory;
import hhplus.tdd.concert.app.domain.repository.payment.AmountHistoryRepository;
import hhplus.tdd.concert.app.domain.repository.payment.PaymentRepository;
import hhplus.tdd.concert.application.service.TestBase;
import hhplus.tdd.concert.common.types.ReserveStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PayServiceUnitTest {

    private final TestBase testBase = new TestBase();

    @InjectMocks
    private PayService payService;

    @Mock
    private WaitingWrapRepository waitingWrapRepository;

    @Mock
    private AmountHistoryRepository amountHistoryRepository;

    @Mock
    private PaymentRepository paymentRepository;

    @Test
    public void 잔액_충전_성공() {
        // when
        when(waitingWrapRepository.findByTokenOrThrow(testBase.waitingToken)).thenReturn(testBase.waitingActive);
        when(amountHistoryRepository.save(any(AmountHistory.class))).thenAnswer(invocation -> {
            AmountHistory amountHistory = invocation.getArgument(0);
            amountHistory.setPointId(1L);
            return amountHistory;
        });
        // then
        UpdateChargeCommand result = payService.chargeAmount(testBase.waitingToken, testBase.amount);

        // 결과검증
        assertNotNull(result);
        verify(waitingWrapRepository).findByTokenOrThrow(testBase.waitingToken);
        verify(amountHistoryRepository).save(any(AmountHistory.class));
        assertEquals(true, result.isCharge());
    }

    @Test
    public void 잔액_조회() {
        // when
        when(waitingWrapRepository.findByTokenOrThrow(testBase.waitingToken)).thenReturn(testBase.waitingActive);

        // then
        LoadAmountQuery result = payService.loadAmount(testBase.waitingToken);

        // 결과검증
        assertEquals(testBase.member.getCharge(), result.amount());
    }

    @Test
    public void 결제_처리_성공() {
        // when
        when(waitingWrapRepository.findByTokenOrThrow(testBase.waitingToken)).thenReturn(testBase.waitingActive);
        when(paymentRepository.findByPayId(eq(1L))).thenReturn(testBase.payment);

        // then
        ReservationCommand result = payService.processPay(testBase.waitingToken, 1L);

        // 결과검증
        assertEquals(testBase.title, result.concertTitle());
        assertEquals(ReserveStatus.RESERVED, result.reserveStatus());
    }

}