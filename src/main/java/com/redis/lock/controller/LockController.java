package com.redis.lock.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.redis.lock.annotation.RedisLock;
import com.redis.lock.service.RedissonService;

@RestController
@RequestMapping("/lock")
public class LockController {

    @Autowired
    private RedissonService redissonService;

    // 验证可重入锁
    @GetMapping("/first")
    @RedisLock(key = "org:redisson:test:lock:first")
    public void processFirst() {
        redissonService.processFirst();
    }

    // 抢占processFirst同一锁, 验证分布式锁
    @GetMapping("/second")
    @RedisLock(key = "org:redisson:test:lock:first")
    public void processSencond() {
        redissonService.processSecond();
    }

    // 验证函数加锁
    @GetMapping("/third")
    public void processThird() {
        redissonService.processThird();
    }
}
