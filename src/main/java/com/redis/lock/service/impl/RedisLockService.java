package com.redis.lock.service.impl;

import com.redis.lock.model.oo.RedisLockOo;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.Optional;
import java.util.function.Function;

/**
 * Author: GL
 * Date: 2022-03-12
 */
@Slf4j
public final class RedisLockService {

    private final RedissonClient redissonClient;

    public <T, R> Optional<R> lock(RedisLockOo redisLock, Optional<T> t, Function<Optional<T>, Optional<R>> func) {
        boolean isLock = false;
        Optional<R> result = Optional.empty();
        final RLock lock = redissonClient.getLock(redisLock.getKey());
        try {
            while (!isLock) {
                isLock = lock.tryLock(redisLock.getWaitTime(), redisLock.getTimeUnit());
                if (isLock) {
                    log.info(" Lock successfully, execute the function ");
                    result = func.apply(t);
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

    public RedisLockService(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }
}
