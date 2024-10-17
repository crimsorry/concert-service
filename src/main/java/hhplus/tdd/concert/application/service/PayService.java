package hhplus.tdd.concert.application.service;

import hhplus.tdd.concert.application.dto.concert.ReservationDto;
import hhplus.tdd.concert.application.dto.concert.SReserveStatus;
import hhplus.tdd.concert.application.dto.payment.LoadAmountDto;
import hhplus.tdd.concert.application.dto.payment.UpdateChargeDto;
import hhplus.tdd.concert.application.dto.waiting.UserDto;
import hhplus.tdd.concert.domain.entity.member.Member;
import hhplus.tdd.concert.domain.entity.payment.AmountHistory;
import hhplus.tdd.concert.domain.entity.payment.PointType;
import hhplus.tdd.concert.domain.entity.waiting.Waiting;
import hhplus.tdd.concert.domain.exception.FailException;
import hhplus.tdd.concert.domain.repository.payment.AmountHistoryRepository;
import hhplus.tdd.concert.domain.repository.waiting.WaitingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PayService {

    private final WaitingRepository waitingRepository;
    private final AmountHistoryRepository amountHistoryRepository;

    /* 잔액 충전 */
    public UpdateChargeDto chargeAmount(String waitingToken, int amount){
        // 대기열 존재 여부 확인
        // TODO: waiting 메소드화 or aop
        Waiting waiting = waitingRepository.findByToken(waitingToken);
        Waiting.checkWaitingExistence(waiting);
        Member member = waiting.getMember();

        // 포인트 충전
        AmountHistory.checkAmountMinus(amount);
        Member.checkMemberCharge(member, amount);
        member.setCharge(member.getCharge() + amount);
        AmountHistory amountHistory = AmountHistory.generateAmountHistory(amount, PointType.CHARGE, waiting.getMember());
        amountHistoryRepository.save(amountHistory);

        return new UpdateChargeDto(true);
    }

    /* 잔액 조회 */
    public LoadAmountDto loadAmount(String waitingToken){
        // 대기열 존재 여부 확인
        Waiting waiting = waitingRepository.findByToken(waitingToken);
        Waiting.checkWaitingExistence(waiting);
        Member member = waiting.getMember();

        // 잔액 조회
        return new LoadAmountDto(member.getCharge());
    }

    /* 결제 처리 */
    public List<ReservationDto> processPay(String waitingToken, long payId){
        List<ReservationDto> reservationDtos = new ArrayList<>();
        reservationDtos.add(new ReservationDto(1L, new UserDto(1L, "김소리", 5000), "콘서트 명", LocalDateTime.of(2024, 10, 15, 12, 1, 1), "A01", 300, SReserveStatus.RESERVED));
        return reservationDtos;
    }

}
