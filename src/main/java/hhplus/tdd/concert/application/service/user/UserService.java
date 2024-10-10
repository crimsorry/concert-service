package hhplus.tdd.concert.application.service.user;

import hhplus.tdd.concert.application.exception.FailException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    /* 잔액 충전 */
    public boolean chargeAmount(String queueToken, int amount){
        if(amount<0){
            throw new FailException("충전 금액이 0 이하입니다.");
        }else if(amount>5000000){
            throw new FailException("충전 한도 초과입니다.");
        }
        return true;
    }

    /* 잔액 조회 */
    public int amount(String queueToken){
        return 300;
    }

}
