package hhplus.tdd.concert.application.service.userqueue;

import hhplus.tdd.concert.application.dto.QueueNumDto;
import hhplus.tdd.concert.application.dto.QueueTokenDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserQueueService {

    /* 유저 대기열 생성 */
    public QueueTokenDto enqueueUser(long userId){
        return new QueueTokenDto("user-token");
    }

    /* 유저 대기열 조회 */
    public QueueNumDto loadQueueUser(String queueToken){
        return new QueueNumDto(1);
    }



}
