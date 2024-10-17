package hhplus.tdd.concert.application.service;

import hhplus.tdd.concert.domain.entity.waiting.Waiting;
import hhplus.tdd.concert.domain.repository.waiting.WaitingRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class BaseService {

    protected final WaitingRepository waitingRepository;

    protected Waiting findAndCheckWaiting(String waitingToken) {
        Waiting waiting = waitingRepository.findByToken(waitingToken);
        Waiting.checkWaitingExistence(waiting);
        return waiting;
    }
}