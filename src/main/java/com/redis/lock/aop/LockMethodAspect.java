package com.redis.lock.aop;

import java.lang.reflect.Method;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.redis.lock.annotation.RedisLock;

/**
 * @description: AOP拦截器
 * @author: gl
 * @create: 2022-03-09
 */
@Slf4j
@Aspect
@Component
public class LockMethodAspect {
    @Autowired
    private RedissonClient redissonClient;

    @Around("@annotation(com.redis.lock.annotation.RedisLock)")
    public Object around(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        RedisLock redisLock = method.getAnnotation(RedisLock.class);

        boolean isLock = false;
        Object result = null;
        final RLock lock = redissonClient.getLock(redisLock.key());
        try {
            while (!isLock) {
                isLock = lock.tryLock(redisLock.waitTime(), redisLock.timeUnit());
                if (isLock) {
                    log.info(" Lock successfully, execute the function ");
                    result = joinPoint.proceed();
                }
                log.info(" Failed to lock. Try to lock");
            }
        } catch (Throwable throwable) {
            log.error(" Throwable locking ", throwable);
        } finally {
            if (isLock) {
                log.info(" Release lock ");
                lock.unlock();
            }
        }
        return result;
    }
}
