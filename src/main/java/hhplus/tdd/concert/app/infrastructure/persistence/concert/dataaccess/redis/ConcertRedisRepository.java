package hhplus.tdd.concert.app.infrastructure.persistence.concert.dataaccess.redis;

import hhplus.tdd.concert.app.domain.concert.entity.Concert;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ConcertRedisRepository {

    private final RedisTemplate<Object, Object> redisTemplate;



}
