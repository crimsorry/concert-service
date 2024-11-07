package hhplus.tdd.concert.app.infrastructure.persistence.waiting.implement;

import hhplus.tdd.concert.app.domain.waiting.repository.WaitingRepository;
import hhplus.tdd.concert.app.infrastructure.persistence.waiting.dataaccess.redis.WaitingRedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
@RequiredArgsConstructor
public class WaitingRepositoryImpl implements WaitingRepository {

    private final WaitingRedisRepository waitingRedisRepository;

    @Override
    public void addWaitingToken(String key, String value, Long currentTime) {
        waitingRedisRepository.addWaitingToken(key, value, currentTime);
    }

    @Override
    public Long getWaitingTokenScore(String key, String value) {
        return waitingRedisRepository.getWaitingTokenScore(key, value);
    }

    @Override
    public Set<String> getWaitingTokenRange(String key, int start, int end) {
        return waitingRedisRepository.getWaitingTokenRange(key, start, end);
    }

    @Override
    public void deleteWaitingToken(String key, String value) {
        waitingRedisRepository.deleteWaitingToken(key, value);
    }

    @Override
    public void addActiveToken(String key, String value) {
        waitingRedisRepository.addActiveToken(key, value);
    }

    @Override
    public Boolean isActiveToken(String key, String value) {
        return waitingRedisRepository.isActiveToken(key, value);
    }

    @Override
    public void deleteActiveToken(String key, String value) {
        waitingRedisRepository.deleteActiveToken(key, value);
    }

}