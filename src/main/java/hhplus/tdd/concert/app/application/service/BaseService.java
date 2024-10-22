package hhplus.tdd.concert.app.application.service;

import hhplus.tdd.concert.app.domain.entity.waiting.Waiting;
import hhplus.tdd.concert.app.domain.repository.waiting.WaitingRepository;
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