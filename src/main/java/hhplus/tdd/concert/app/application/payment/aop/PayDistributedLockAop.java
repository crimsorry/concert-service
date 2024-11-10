package hhplus.tdd.concert.app.application.payment.aop;

import hhplus.tdd.concert.app.application.payment.dto.UpdateChargeCommand;
import hhplus.tdd.concert.app.application.reservation.dto.ReservationCommand;
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
public class PayDistributedLockAop {

    private static final String REDISSON_LOCK_PREFIX = "LOCK:";

    private final RedissonClient redissonClient; // RedissonClient를 통한 Redis 분산 락 관리
    private final PayAopForTransaction aopTransaction; // 트랜잭션 내에서 joinPoint를 실행하는 헬퍼 클래스

    @Around("@annotation(hhplus.tdd.concert.config.aop.DistributedLock) && args(waitingToken, amount, ..)")
    public UpdateChargeCommand chargeAmountRedisPubSub(String waitingToken, int amount){
        String lockName = REDISSON_LOCK_PREFIX + "user_id:" + waitingToken;
        RLock lock = redissonClient.getLock(lockName);
        boolean isOK = false;

        try{
            isOK = lock.tryLock(10, 1, TimeUnit.SECONDS);
            if(!isOK){
                throw new FailException(ErrorCode.REDIS_LOCK_NOT_AVAILABLE, LogLevel.WARN);
            }
            return aopTransaction.processChargeAmount(waitingToken, amount);
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

    @Around("@annotation(hhplus.tdd.concert.config.aop.DistributedLock) && args(waitingToken, payId, ..)")
    public ReservationCommand processPayRedisPubSub(String waitingToken, long payId){
        String lockName = REDISSON_LOCK_PREFIX + "user_id:" + waitingToken;
        RLock lock = redissonClient.getLock(lockName);
        boolean isOK = false;

        try{
            isOK = lock.tryLock(10, 1, TimeUnit.SECONDS);
            if(!isOK){
                throw new FailException(ErrorCode.REDIS_LOCK_NOT_AVAILABLE, LogLevel.WARN);
            }
            return aopTransaction.processPay(waitingToken, payId);
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
