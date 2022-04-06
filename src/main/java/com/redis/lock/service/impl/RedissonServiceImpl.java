package com.redis.lock.service.impl;

import java.util.Optional;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import com.redis.lock.annotation.RedisLock;
import com.redis.lock.model.oo.RedisLockOo;
import com.redis.lock.service.RedissonService;

/**
 * Author: GL
 * Date: 2022-03-09
 */
@Slf4j
@Service
@RestController
public class RedissonServiceImpl implements RedissonService {

    @Autowired
    private RedisLockService redisLockService; // 非注解方式加锁

    @Override
    @RedisLock(key = "org:redisson:test:lock:first") // 再次加锁, 验证可重入锁
    public void processFirst() {
        log.info("RedissonServiceImpl:processFirst() process");
        try {
            Thread.sleep(1000 * 70); // 默认续约为30s, 验证锁续约
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void processSecond() {
        log.info("RedissonServiceImpl:processSecond() process");
        try {
            Thread.sleep(1000 * 5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void processThird() {
        log.info("RedissonServiceImpl:processThird() process");

        final RedisLockOo lockOo = RedisLockOo
                .builder()
                .key("org:redisson:test:lock:first")
                .build();

        redisLockService.lock(lockOo, Optional.of(1000 * 20), (p) -> {
            try {
                Thread.sleep(p.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return Optional.empty();
        });

    }
}
