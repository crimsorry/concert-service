package hhplus.tdd.concert.application.service;

import hhplus.tdd.concert.application.dto.concert.ReservationDto;
import hhplus.tdd.concert.application.dto.concert.SReserveStatus;
import hhplus.tdd.concert.application.dto.payment.LoadAmountDto;
import hhplus.tdd.concert.application.dto.payment.UpdateChargeDto;
import hhplus.tdd.concert.application.dto.waiting.UserDto;
import hhplus.tdd.concert.application.exception.FailException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PayService {

    /* 잔액 충전 */
    public UpdateChargeDto chargeAmount(String waitingToken, int amount){
        if(amount<0){
            throw new FailException("충전 금액이 0 이하입니다.");
        }else if(amount>5000000){
            throw new FailException("충전 한도 초과입니다.");
        }
        return new UpdateChargeDto(true);
    }

    /* 잔액 조회 */
    public LoadAmountDto loadAmount(String waitingToken){
        return new LoadAmountDto(300);
    }

    /* 결제 처리 */
    public List<ReservationDto> processPay(String waitingToken, long payId){
        List<ReservationDto> reservationDtos = new ArrayList<>();
        reservationDtos.add(new ReservationDto(1L, new UserDto(1L, "김소리", 5000), "콘서트 명", LocalDateTime.of(2024, 10, 15, 12, 1, 1), "A01", 300, SReserveStatus.RESERVED));
        return reservationDtos;
    }

}
