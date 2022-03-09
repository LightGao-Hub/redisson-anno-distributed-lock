package com.redis.lock.service;

/**
 * Author: GL
 * Date: 2022-03-09
 */
public interface RedissonService {
    void processFirst();
    void processSecond();
}
