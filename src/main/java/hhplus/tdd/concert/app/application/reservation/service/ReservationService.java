package hhplus.tdd.concert.app.application.reservation.service;

import hhplus.tdd.concert.app.application.payment.dto.PayDTO;
import hhplus.tdd.concert.app.application.reservation.dto.ReservationDTO;
import hhplus.tdd.concert.app.domain.concert.entity.ConcertSeat;
import hhplus.tdd.concert.app.domain.concert.repository.ConcertSeatRepository;
import hhplus.tdd.concert.app.domain.exception.ErrorCode;
import hhplus.tdd.concert.app.domain.openapi.event.KakaoMsgEvent;
import hhplus.tdd.concert.app.domain.openapi.event.KakaoProcessPublisher;
import hhplus.tdd.concert.app.domain.payment.entity.Payment;
import hhplus.tdd.concert.app.domain.payment.repository.PaymentRepository;
import hhplus.tdd.concert.app.domain.reservation.entity.Reservation;
import hhplus.tdd.concert.app.domain.reservation.repository.ReservationRepository;
import hhplus.tdd.concert.app.domain.waiting.entity.ActiveToken;
import hhplus.tdd.concert.app.domain.waiting.entity.Member;
import hhplus.tdd.concert.app.domain.waiting.repository.MemberRepository;
import hhplus.tdd.concert.app.domain.waiting.repository.WaitingRepository;
import hhplus.tdd.concert.config.exception.FailException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.logging.LogLevel;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ConcertSeatRepository concertSeatRepository;
    private final ReservationRepository reservationRepository;
    private final PaymentRepository paymentRepository;
    private final WaitingRepository waitingRepository;
    private final MemberRepository memberRepository;

    private final KakaoProcessPublisher kakaoProcessPublisher;

    /* 좌석 예약 요청 */
    @Transactional
    public PayDTO processReserve(String waitingToken, Long seatId){
        // 비관적 락
        ConcertSeat concertSeat = concertSeatRepository.findBySeatIdWithPessimisticLock(seatId);

        // 좌석 상태 확인
        ConcertSeat.checkConcertSeatExistence(concertSeat);
        ConcertSeat.checkConcertSeatStatus(concertSeat);

        ActiveToken activeToken = waitingRepository.findByTokenOrThrow(waitingToken)
                .orElseThrow(() -> new FailException(ErrorCode.NOT_FOUND_WAITING_MEMBER, LogLevel.ERROR));
        long memberId = activeToken.getMemberId();
        Member member = memberRepository.findByMemberId(memberId);

        Reservation reservation = Reservation.generateReservation(member, concertSeat);
        Payment payment = Payment.generatePayment(member, reservation);
        payment.recordExpiredAt();

        // 좌석 임시배정
        concertSeat.pending();
        reservationRepository.save(reservation);
        paymentRepository.save(payment);

        // event listener : 예약 완료 > 카카오톡 전송
        PayDTO payDto = PayDTO.from(payment, reservation);
        KakaoMsgEvent kakaoMsgEvent = new KakaoMsgEvent(
                "[입금요망]",
                member.getMemberName(),
                reservation.getConcertTitle(),
                reservation.getAmount(),
                reservation.getSeatCode());
        kakaoProcessPublisher.publishEvent(kakaoMsgEvent);

        return payDto;
    }

    @Transactional
    public PayDTO processReserveOptimisticLock(String waitingToken, Long seatId){
        ConcertSeat concertSeat = concertSeatRepository.findBySeatIdWithOptimisticLock(seatId);

        // 좌석 상태 확인
        ConcertSeat.checkConcertSeatExistence(concertSeat);
        ConcertSeat.checkConcertSeatStatus(concertSeat);

        ActiveToken activeToken = waitingRepository.findByTokenOrThrow(waitingToken)
                .orElseThrow(() -> new FailException(ErrorCode.NOT_FOUND_WAITING_MEMBER, LogLevel.ERROR));
        long memberId = activeToken.getMemberId();
        Member member = memberRepository.findByMemberId(memberId);

        Reservation reservation = Reservation.generateReservation(member, concertSeat);
        Payment payment = Payment.generatePayment(member, reservation);

        // 좌석 임시배정
        concertSeat.pending();
//        waiting.limitPayTime();
        reservationRepository.save(reservation);
        paymentRepository.save(payment);

        return PayDTO.from(payment, reservation);
    }

    @Transactional
    @Retryable(
            retryFor = ObjectOptimisticLockingFailureException.class,
            maxAttempts = 100,
            backoff = @Backoff(100)
    )
    public PayDTO processReserveOptimisticLockRetry(String waitingToken, Long seatId){
        ConcertSeat concertSeat = concertSeatRepository.findBySeatIdWithOptimisticLock(seatId);

        // 좌석 상태 확인
        ConcertSeat.checkConcertSeatExistence(concertSeat);
        ConcertSeat.checkConcertSeatStatus(concertSeat);

        ActiveToken activeToken = waitingRepository.findByTokenOrThrow(waitingToken)
                .orElseThrow(() -> new FailException(ErrorCode.NOT_FOUND_WAITING_MEMBER, LogLevel.ERROR));
        long memberId = activeToken.getMemberId();
        Member member = memberRepository.findByMemberId(memberId);

        Reservation reservation = Reservation.generateReservation(member, concertSeat);
        Payment payment = Payment.generatePayment(member, reservation);

        // 좌석 임시배정
        concertSeat.pending();
//        waiting.limitPayTime();
        reservationRepository.save(reservation);
        paymentRepository.save(payment);

        return PayDTO.from(payment, reservation);
    }

    /* 예약 조회 */
    public List<ReservationDTO> loadReservation(String waitingToken){
        ActiveToken activeToken = waitingRepository.findByTokenOrThrow(waitingToken)
                .orElseThrow(() -> new FailException(ErrorCode.NOT_FOUND_WAITING_MEMBER, LogLevel.ERROR));
        long memberId = activeToken.getMemberId();
        Member member = memberRepository.findByMemberId(memberId);

        List<Reservation> reservations = reservationRepository.findByMember(member);
        return ReservationDTO.from(reservations);
    }

}
