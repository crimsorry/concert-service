package hhplus.tdd.concert.application.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    /* 잔액 충전 */
    public boolean chargeAmount(String queueToken, int amount){
        return true;
    }

    /* 잔액 조회 */
    public int amount(String queueToken){
        return 300;
    }

}
