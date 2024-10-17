package hhplus.tdd.concert.application.service.payment;

import hhplus.tdd.concert.application.dto.concert.ReservationDto;
import hhplus.tdd.concert.application.dto.concert.SReserveStatus;
import hhplus.tdd.concert.application.dto.payment.LoadAmountDto;
import hhplus.tdd.concert.application.dto.payment.UpdateChargeDto;
import hhplus.tdd.concert.domain.entity.concert.*;
import hhplus.tdd.concert.domain.entity.member.Member;
import hhplus.tdd.concert.domain.entity.payment.AmountHistory;
import hhplus.tdd.concert.domain.entity.payment.Payment;
import hhplus.tdd.concert.domain.entity.payment.PointType;
import hhplus.tdd.concert.domain.entity.waiting.Waiting;
import hhplus.tdd.concert.domain.entity.waiting.WaitingStatus;
import hhplus.tdd.concert.domain.exception.FailException;
import hhplus.tdd.concert.application.service.PayService;
import hhplus.tdd.concert.domain.repository.payment.AmountHistoryRepository;
import hhplus.tdd.concert.domain.repository.payment.PaymentRepository;
import hhplus.tdd.concert.domain.repository.waiting.WaitingRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PayServiceUnitTest {

    @InjectMocks
    private PayService payService;

    @Mock
    private WaitingRepository waitingRepository;

    @Mock
    private AmountHistoryRepository amountHistoryRepository;

    @Mock
    private PaymentRepository paymentRepository;

    // TODO: 테스트 코드 전역 변수 공통으로 묶기.
    private final int amount = 500;
    private final LocalDateTime now = LocalDateTime.now();
    private final String waitingToken = "testToken";
    private final Member member = new Member(1L, "김소리", 150000);
    private final Waiting waiting = new Waiting(1L, member, waitingToken, WaitingStatus.STAND_BY, now, LocalDateTime.now().plusMinutes(30));
    private final AmountHistory amountHistory = new AmountHistory(1L, member, amount, PointType.CHARGE, LocalDateTime.now());
    private final Concert concert = new Concert(1L, "드라큘라", "부산문화회관 대극장");
    private final ConcertSchedule concertSchedule = new ConcertSchedule(1L, concert, now, LocalDateTime.now().minusDays(1), now.plusDays(1), 50);
    private final ConcertSeat concertSeat = new ConcertSeat(1L, concertSchedule, "A01", 140000, SeatStatus.RESERVED);
    private final Reservation reservation = new Reservation(1L, member, concertSeat, "드라큘라", LocalDateTime.now(), "A01", 140000, ReserveStatus.PENDING);
    private final Payment payment = new Payment(1L, member, reservation, 140000, false, now);

    @Test
    public void 잔액_충전_성공() {
        // when
        // TODO when @beforeEach 초기화
        when(waitingRepository.findByToken(eq(waitingToken))).thenReturn(waiting);
        when(amountHistoryRepository.save(any(AmountHistory.class))).thenAnswer(invocation -> {
            AmountHistory amountHistory = invocation.getArgument(0);
            amountHistory.setPointId(1L); // save 후에 ID가 생성됨을 가정
            return amountHistory;
        });
        // then
        UpdateChargeDto result = payService.chargeAmount(waitingToken, amount);

        // 결과검증
        assertNotNull(result);
        verify(waitingRepository).findByToken(waitingToken);
        verify(amountHistoryRepository).save(any(AmountHistory.class));
        assertEquals(true, result.isCharge());
    }

    @Test
    public void 잔액_조회() {
        // when
        when(waitingRepository.findByToken(eq(waitingToken))).thenReturn(waiting);

        // then
        LoadAmountDto result = payService.loadAmount(waitingToken);

        // 결과검증
        assertEquals(0, result.amount());
    }

    @Test
    public void 결제_처리_성공() {
        // when
        when(waitingRepository.findByToken(eq(waitingToken))).thenReturn(waiting);
        when(paymentRepository.findByPaymentId(eq(1L))).thenReturn(payment);

        // then
        ReservationDto result = payService.processPay(waitingToken, 1L);

        // 결과검증
        assertEquals("드라큘라", result.concertTitle());
        assertEquals(SReserveStatus.RESERVED, result.reserveStatus());
    }

}