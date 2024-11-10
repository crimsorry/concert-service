package hhplus.tdd.concert.app.application.reservation.aop;

import hhplus.tdd.concert.app.application.payment.dto.PayCommand;
import hhplus.tdd.concert.app.domain.exception.ErrorCode;
import hhplus.tdd.concert.config.exception.FailException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.boot.logging.LogLevel;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Order(1)
@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class ReserveDistributedLockAop {

    private static final String REDISSON_LOCK_PREFIX = "LOCK:";

    private final RedissonClient redissonClient; // RedissonClient를 통한 Redis 분산 락 관리
    private final ReserveAopForTransaction aopTransaction; // 트랜잭션 내에서 joinPoint를 실행하는 헬퍼 클래스

    @Around("@annotation(hhplus.tdd.concert.config.aop.DistributedLock) && args(waitingToken, seatId, ..)")
    public PayCommand processReserveRedisPubSub(String waitingToken, Long seatId){
        String lockName = REDISSON_LOCK_PREFIX + "seat_id:" + seatId;
        RLock lock = redissonClient.getLock(lockName);
        boolean isOK = false;

        try{
            isOK = lock.tryLock(10, 1, TimeUnit.SECONDS);
            if(!isOK){
                throw new FailException(ErrorCode.REDIS_LOCK_NOT_AVAILABLE, LogLevel.WARN);
            }
            return aopTransaction.processChargeAmount(waitingToken, seatId);
        }catch (InterruptedException e){ // 에러
            throw new FailException(ErrorCode.REDIS_LOCK_INTERRUPTED, LogLevel.WARN);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        } finally {
            if (isOK) {
                lock.unlock();
            }
        }
    }

}
