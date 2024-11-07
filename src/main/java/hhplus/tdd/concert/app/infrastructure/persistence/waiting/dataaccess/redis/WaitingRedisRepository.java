package hhplus.tdd.concert.app.infrastructure.persistence.waiting.dataaccess.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class WaitingRedisRepository {

    private final RedisTemplate<String, Object> redisTemplate;

    public void addWaitingToken(String key, Object value, Long currentTime) {
        redisTemplate.opsForZSet().add(key, value, currentTime);
    }

    public Long getWaitingTokenScore(String key, Object value) {
        return redisTemplate.opsForZSet().rank(key, value);
    }

    public Boolean isWaitingTokenKey(String key) {
        return redisTemplate.hasKey(key);
    }

    public Set<Object> getWaitingTokenRange(String key, int start, int end) {
        return redisTemplate.opsForZSet().range(key, start, end);
    }

    public void deleteWaitingToken(String key, Object value) {
        redisTemplate.opsForZSet().remove(key, value);
    }

    public void addActiveToken(String key, Object value) {
        redisTemplate.opsForSet().add(key, value);
    }

    public Set<Object> getAllTokens(String key) {
        return redisTemplate.opsForZSet().range(key, 0, -1);

    }

    public ZSetOperations<String, Object> getWaitingToken() {
        return redisTemplate.opsForZSet();
    }

    public SetOperations<String, Object> getActiveToken() {
        return redisTemplate.opsForSet();
    }

    public Boolean isActiveToken(String key, long memberId) {
        return redisTemplate.opsForSet().isMember(key, memberId);
    }

    public void deleteActiveToken(String key, Object value) {
        redisTemplate.opsForSet().remove(key, value);
    }


}
