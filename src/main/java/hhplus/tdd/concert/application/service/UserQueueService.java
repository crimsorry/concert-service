package hhplus.tdd.concert.application.service;

import hhplus.tdd.concert.application.dto.QueueNumDto;
import hhplus.tdd.concert.application.dto.QueueTokenDto;
import hhplus.tdd.concert.application.exception.FailException;
import hhplus.tdd.concert.domain.entity.user.User;
import hhplus.tdd.concert.domain.repository.user.UserRepository;
import hhplus.tdd.concert.domain.repository.userqueue.UserQueueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserQueueService {

    private UserRepository userRepository;
    private UserQueueRepository userQueueRepository;

    /* 유저 대기열 생성 */
    public QueueTokenDto enqueueUser(long userId){
        return new QueueTokenDto("user-token");
    }

    /* 유저 대기열 조회 */
    public QueueNumDto loadQueueUser(String queueToken){
        return new QueueNumDto(1);
    }



}
