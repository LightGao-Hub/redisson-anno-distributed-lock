package com.redis.lock.model.oo;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.concurrent.TimeUnit;

/**
 * Author: GL
 * Date: 2022-03-12
 */
@Builder
@Data
public class RedisLockOo {
    @NotNull
    private final String key;
    @Builder.Default
    private int expire = 30;
    @Builder.Default
    private long waitTime = 1;
    @Builder.Default
    private TimeUnit timeUnit = TimeUnit.SECONDS;
}
