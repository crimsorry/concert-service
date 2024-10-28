package hhplus.tdd.concert.app.domain.repository.waiting.wrapper;

import hhplus.tdd.concert.app.domain.entity.waiting.Waiting;
import hhplus.tdd.concert.app.domain.repository.waiting.WaitingRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class WaitingWrapRepository {

    private final WaitingRepository waitingRepository;

    public Waiting findByTokenOrThrow(String waitingToken) {
        Waiting waiting = waitingRepository.findByToken(waitingToken);
        Waiting.checkWaitingExistence(waiting);
        return waiting;
    }

}