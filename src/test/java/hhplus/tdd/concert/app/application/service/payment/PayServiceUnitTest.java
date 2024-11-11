package hhplus.tdd.concert.app.application.service.payment;

import hhplus.tdd.concert.app.application.payment.dto.LoadAmountDTO;
import hhplus.tdd.concert.app.application.payment.dto.UpdateChargeDTO;
import hhplus.tdd.concert.app.application.payment.service.PayService;
import hhplus.tdd.concert.app.application.reservation.dto.ReservationDTO;
import hhplus.tdd.concert.app.application.service.TestBase;
import hhplus.tdd.concert.app.domain.payment.entity.AmountHistory;
import hhplus.tdd.concert.app.domain.payment.repository.AmountHistoryRepository;
import hhplus.tdd.concert.app.domain.payment.repository.PaymentRepository;
import hhplus.tdd.concert.app.domain.waiting.repository.WaitingRepository;
import hhplus.tdd.concert.config.types.ReserveStatus;
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
    private WaitingRepository waitingRepository;

    @Mock
    private AmountHistoryRepository amountHistoryRepository;

    @Mock
    private PaymentRepository paymentRepository;

    @Test
    public void 잔액_충전_성공() {
        // when
        when(waitingRepository.findByTokenOrThrow(testBase.waitingToken).get()).thenReturn(testBase.activeToken);
        when(amountHistoryRepository.save(any(AmountHistory.class))).thenAnswer(invocation -> {
            AmountHistory amountHistory = invocation.getArgument(0);
            return amountHistory.builder()
                    .pointId(1L)
                    .build();
        });
        // then
        UpdateChargeDTO result = payService.chargeAmount(testBase.waitingToken, testBase.amount);

        // 결과검증
        assertNotNull(result);
        verify(waitingRepository).findByTokenOrThrow(testBase.waitingToken);
        verify(amountHistoryRepository).save(any(AmountHistory.class));
        assertEquals(true, result.isCharge());
    }

    @Test
    public void 잔액_조회() {
        // when
        when(waitingRepository.findByTokenOrThrow(testBase.waitingToken).get()).thenReturn(testBase.activeToken);

        // then
        LoadAmountDTO result = payService.loadAmount(testBase.waitingToken);

        // 결과검증
        assertEquals(testBase.member.getCharge(), result.amount());
    }

    @Test
    public void 결제_처리_성공() {
        // when
        when(waitingRepository.findByTokenOrThrow(testBase.waitingToken).get()).thenReturn(testBase.activeToken);
        when(paymentRepository.findByPayId(eq(1L))).thenReturn(testBase.payment);

        // then
        ReservationDTO result = payService.processPay(testBase.waitingToken, 1L);

        // 결과검증
        assertEquals(testBase.title, result.concertTitle());
        assertEquals(ReserveStatus.RESERVED, result.reserveStatus());
    }

}