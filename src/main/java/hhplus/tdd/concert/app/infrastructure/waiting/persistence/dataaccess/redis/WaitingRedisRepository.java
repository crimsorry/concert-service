package hhplus.tdd.concert.app.infrastructure.waiting.persistence.dataaccess.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
@RequiredArgsConstructor
public class WaitingRedisRepository {

    private final RedisTemplate<String, String> redisTemplate;

    public void addWaitingToken(String key, String value, Long currentTime) {
        redisTemplate.opsForZSet().add(key, value, currentTime);
    }

    public Long getWaitingTokenScore(String key, String value) {
        return redisTemplate.opsForZSet().rank(key, value);
    }

    public Boolean isWaitingTokenKey(String key) {
        return redisTemplate.hasKey(key);
    }

    public Set<String> getWaitingTokenRange(String key, int start, int end) {
        return redisTemplate.opsForZSet().range(key, start, end);
    }

    public void deleteWaitingToken(String key, String value) {
        redisTemplate.opsForZSet().remove(key, value);
    }

    public void addActiveToken(String key, String value) {
        redisTemplate.opsForSet().add(key, value);
    }

    public Set<String> getAllTokens(String key) {
        return redisTemplate.opsForZSet().range(key, 0, -1);
    }

    public ZSetOperations<String, String> getWaitingToken() {
        return redisTemplate.opsForZSet();
    }

    public SetOperations<String, String> getActiveToken() {
        return redisTemplate.opsForSet();
    }

    public Boolean isActiveToken(String key, String value) {
        return redisTemplate.opsForSet().isMember(key, value);
    }

    public void deleteActiveToken(String key, String value) {
        redisTemplate.opsForSet().remove(key, value);
    }
}
