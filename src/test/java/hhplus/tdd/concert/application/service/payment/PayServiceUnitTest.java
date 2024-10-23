package hhplus.tdd.concert.application.service.payment;

import hhplus.tdd.concert.app.application.dto.reservation.ReservationDto;
import hhplus.tdd.concert.app.application.dto.payment.LoadAmountDto;
import hhplus.tdd.concert.app.application.dto.payment.UpdateChargeDto;
import hhplus.tdd.concert.app.application.service.payment.PayService;
import hhplus.tdd.concert.app.domain.entity.payment.AmountHistory;
import hhplus.tdd.concert.app.domain.repository.payment.AmountHistoryRepository;
import hhplus.tdd.concert.app.domain.repository.payment.PaymentRepository;
import hhplus.tdd.concert.app.domain.repository.waiting.WaitingRepository;
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
    private WaitingRepository waitingRepository;

    @Mock
    private AmountHistoryRepository amountHistoryRepository;

    @Mock
    private PaymentRepository paymentRepository;

    @Test
    public void 잔액_충전_성공() {
        // when
        when(waitingRepository.findByToken(eq(testBase.waitingToken))).thenReturn(testBase.waiting);
        when(amountHistoryRepository.save(any(AmountHistory.class))).thenAnswer(invocation -> {
            AmountHistory amountHistory = invocation.getArgument(0);
            amountHistory.setPointId(1L); // save 후에 ID가 생성됨을 가정
            return amountHistory;
        });
        // then
        UpdateChargeDto result = payService.chargeAmount(testBase.waitingToken, testBase.amount);

        // 결과검증
        assertNotNull(result);
        verify(waitingRepository).findByToken(testBase.waitingToken);
        verify(amountHistoryRepository).save(any(AmountHistory.class));
        assertEquals(true, result.isCharge());
    }

    @Test
    public void 잔액_조회() {
        // when
        when(waitingRepository.findByToken(eq(testBase.waitingToken))).thenReturn(testBase.waiting);

        // then
        LoadAmountDto result = payService.loadAmount(testBase.waitingToken);

        // 결과검증
        assertEquals(testBase.member.getCharge(), result.amount());
    }

    @Test
    public void 결제_처리_성공() {
        // when
        when(waitingRepository.findByToken(eq(testBase.waitingToken))).thenReturn(testBase.waiting);
        when(paymentRepository.findByPayId(eq(1L))).thenReturn(testBase.payment);

        // then
        ReservationDto result = payService.processPay(testBase.waitingToken, 1L);

        // 결과검증
        assertEquals(testBase.title, result.concertTitle());
        assertEquals(ReserveStatus.RESERVED, result.reserveStatus());
    }

}