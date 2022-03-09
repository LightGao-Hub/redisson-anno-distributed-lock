package com.redis.lock.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface RedisLock {
    /**
     * 业务键
     *
     * @return
     */
    String key();

    /**
     * 锁的过期秒数, 默认是30s, 由于使用redisson的自动续约，故此参数后续不使用
     *
     * @return
     */
    int expire() default 30;

    /**
     * 尝试加锁，最多等待时间1s
     *
     * @return
     */
    long waitTime() default 1;

    /**
     * 锁的超时时间单位
     *
     * @return
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;
}
