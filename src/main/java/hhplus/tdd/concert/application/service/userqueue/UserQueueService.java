package hhplus.tdd.concert.application.service.userqueue;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserQueueService {

    /* 유저 대기열 생성 */
    public String enqueueUser(long userId){
        return "user-token";
    }



}
