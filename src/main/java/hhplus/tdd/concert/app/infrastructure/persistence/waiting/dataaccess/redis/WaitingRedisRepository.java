//package hhplus.tdd.concert.app.infrastructure.persistence.waiting.dataaccess.redis;
//
//import hhplus.tdd.concert.app.domain.waiting.entity.Waiting;
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.stereotype.Repository;
//
//import java.time.ZoneOffset;
//import java.util.Set;
//
//@Repository
//@RequiredArgsConstructor
//public class WaitingRedisRepository {
//
//    private final RedisTemplate<Object, Object> redisTemplate;
//
////    public void insertQueue(String key, Object value){
////        redisTemplate.opsForZSet().add(key, value);
////    }
//
//    public void save(String key, Waiting waiting, double score) {
////        double score = waiting.getCreateAt().toEpochSecond(ZoneOffset.ofHours(9)); // 생성 시간을 score로 사용
//        redisTemplate.opsForZSet().add(key, waiting, score);
//    }
//
//    public Waiting findWaitingByToken(String key, String token) {
//        Set<Object> waitings = redisTemplate.opsForZSet().range(key, 0, -1);
//        if (waitings != null) {
//            return (Waiting) waitings.stream()
//                    .filter(waiting -> ((Waiting) waiting).getToken().equals(token))
//                    .findFirst()
//                    .orElse(null);
//        }
//        return null;
//    }
//
//
//}
