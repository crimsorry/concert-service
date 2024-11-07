package hhplus.tdd.concert.app.infrastructure.persistence.waiting.implement;

import hhplus.tdd.concert.app.domain.waiting.entity.ActiveToken;
import hhplus.tdd.concert.app.domain.waiting.repository.WaitingRepository;
import hhplus.tdd.concert.app.infrastructure.persistence.waiting.dataaccess.redis.WaitingRedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
    public List<ActiveToken> getWaitingTokenRange(String key, int start, int end) {
        List<ActiveToken> tokens = new ArrayList<>();
        waitingRedisRepository.getWaitingTokenRange(key, start, end).forEach(token ->{
            tokens.add(new ActiveToken(token.split(":")[0], Long.parseLong(token.split(":")[1]), 0L));
        });
        return tokens;
    }

    @Override
    public void deleteWaitingToken(String key, String value) {
        waitingRedisRepository.deleteWaitingToken(key, value);
    }

    @Override
    public List<ActiveToken> getActiveToken(String key){
        List<ActiveToken> tokens = new ArrayList<>();
        waitingRedisRepository.getActiveToken().members(key).forEach(token ->{
            tokens.add(new ActiveToken(token.split(":")[0], Long.parseLong(token.split(":")[1]), Long.parseLong(token.split(":")[2])));
        });
        return tokens;
    }

    @Override
    public List<ActiveToken> getWaitingToken(String key) {
        List<ActiveToken> tokens = new ArrayList<>();
        Set<String> tokenSet = waitingRedisRepository.getWaitingToken().range(key, 0, -1); // 전체 범위를 가져옵니다.

        if (tokenSet != null) {
            tokenSet.forEach(token -> {
                tokens.add(new ActiveToken(token.split(":")[0], Long.parseLong(token.split(":")[1]), 0L));
            });
        }
        return tokens;
    }

    @Override
    public Set<String> getAllTokens(String key) {
        return waitingRedisRepository.getAllTokens(key);
    }

    @Override
    public void deleteActiveToken(String key, String value) {
        waitingRedisRepository.deleteActiveToken(key, value);
    }

    @Override
    public void addActiveToken(String key, String value) {
        waitingRedisRepository.addActiveToken(key, value);
    }

    @Override
    public Optional<ActiveToken> findByTokenOrThrow(String waitingToken) {
        Set<String> tokens = waitingRedisRepository.getActiveToken().members("activeToken");

        if (tokens == null || tokens.isEmpty()) {
            return Optional.empty();
        }

        return tokens.stream()
                .map(token -> new ActiveToken(token.split(":")[0], Long.parseLong(token.split(":")[1]), Long.parseLong(token.split(":")[2])))
                .filter(activeToken -> activeToken.getToken().split(":")[0].equals(waitingToken))
                .findFirst();
    }

}