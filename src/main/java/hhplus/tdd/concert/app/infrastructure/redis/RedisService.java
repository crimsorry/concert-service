//package hhplus.tdd.concert.app.infrastructure.redis;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.stereotype.Service;
//
//@Service
//@RequiredArgsConstructor
//public class RedisService {
//
//    private final RedisTemplate<Object, Object> redisTemplate;
//
//    public void saveObjectData(Object key, Object value) {
//        redisTemplate.opsForValue().set(key, value);
//    }
//
//    public Object getObjectData(Object key) {
//        return redisTemplate.opsForValue().get(key);
//    }
//}