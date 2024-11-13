package io.hhplus.commerce.common.aop;

import io.hhplus.commerce.common.annotation.DistributedLock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Method;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class DistributedLockAop {
    private static final String REDISSON_LOCK_PREFIX = "LOCK:";

    private final RedissonClient redissonClient;
    private final AopForTransaction aopForTransaction;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Around("@annotation(io.hhplus.commerce.common.annotation.DistributedLock)")
    @Transactional
    public Object lock(final ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        DistributedLock distributedLock = method.getAnnotation(DistributedLock.class);

        String key = REDISSON_LOCK_PREFIX + CustomSpringELParser.getDynamicValue(signature.getParameterNames(), joinPoint.getArgs(), distributedLock.key());
        RLock rLock = redissonClient.getLock(key);

        try {
            log.info("--- DistributedLockAop --- 락 획득 시도, {}", key);
            if (rLock.tryLock(distributedLock.waitTime(), distributedLock.leaseTime(), distributedLock.timeUnit())) {
                return aopForTransaction.proceed(joinPoint);
            }
            throw new RuntimeException("Redisson Lock Already Lock");
        } catch (InterruptedException e) {
            throw new InterruptedException();
        } finally {
            if (rLock.isLocked() && rLock.isHeldByCurrentThread()) {
                log.info("--- DistributedLockAop --- 락 반납 시도, {}", key);
                applicationEventPublisher.publishEvent(rLock);
            }
        }
    }
}
