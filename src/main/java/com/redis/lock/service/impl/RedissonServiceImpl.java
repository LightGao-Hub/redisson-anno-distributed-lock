package com.redis.lock.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import com.redis.lock.annotation.RedisLock;
import com.redis.lock.service.RedissonService;

/**
 * Author: GL
 * Date: 2022-03-09
 */
@Slf4j
@Service
@RestController
public class RedissonServiceImpl implements RedissonService {
    @Override
    @RedisLock(key = "org:redisson:test:lock:first") // 验证可重入锁
    public void processFirst() {
        log.info("RedissonServiceImpl:processFirst() process");
        try {
            Thread.sleep(1000 * 70); // 默认续约为30分钟, 验证续约
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
}
