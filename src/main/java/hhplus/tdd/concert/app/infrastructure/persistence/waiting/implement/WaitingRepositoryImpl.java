package hhplus.tdd.concert.app.infrastructure.persistence.waiting.implement;

import hhplus.tdd.concert.app.domain.waiting.model.ActiveToken;
import hhplus.tdd.concert.app.domain.waiting.repository.WaitingRepository;
import hhplus.tdd.concert.app.infrastructure.persistence.waiting.dataaccess.redis.WaitingRedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class WaitingRepositoryImpl implements WaitingRepository {

    private final WaitingRedisRepository waitingRedisRepository;

    @Override
    public void addWaitingToken(String key, Object value, Long currentTime) {
        waitingRedisRepository.addWaitingToken(key, value, currentTime);
    }

    @Override
    public Long getWaitingTokenScore(String key, Object value) {
        return waitingRedisRepository.getWaitingTokenScore(key, value);
    }

    @Override
    public Boolean isWaitingTokenKey(String key) {
        return waitingRedisRepository.isWaitingTokenKey(key);
    }

    @Override
    public Set<Object> getWaitingTokenRange(String key, int start, int end) {
        return waitingRedisRepository.getWaitingTokenRange(key, start, end);
    }

    @Override
    public void deleteWaitingToken(String key, Object value) {
        waitingRedisRepository.deleteWaitingToken(key, value);
    }

    @Override
    public List<ActiveToken> getActiveToken(String key){
        List<ActiveToken> tokens = new ArrayList<>();
        waitingRedisRepository.getActiveToken().members(key).forEach(token ->{
            tokens.add(new ActiveToken(null, token.toString(), null));
        });
        return tokens;
    }

    @Override
    public ZSetOperations<String, Object> getWaitingToken() {
        return waitingRedisRepository.getWaitingToken();
    }

    @Override
    public Set<Object> getAllTokens(String key) {
        return waitingRedisRepository.getAllTokens(key);
    }

    @Override
    public Boolean isActiveToken(String key, long memberId) {
        return waitingRedisRepository.isActiveToken(key, memberId);
    }

    @Override
    public void deleteActiveToken(String key, Object value) {
        waitingRedisRepository.deleteActiveToken(key, value);
    }

    @Override
    public void addActiveToken(String key, Object value) {
        waitingRedisRepository.addActiveToken(key, value);
    }

}