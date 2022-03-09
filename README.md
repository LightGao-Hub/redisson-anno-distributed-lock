## 前言

    此项目使用redis-redisson实现分布式锁, 由于redisson天然支持可重入锁&自动续约&redis多种集群适配, 此项目案例使用aop及注解的方式实现此分布式锁项目
    
    在企业开发中，分布式锁十分常见，但根据redis做好分布式锁需要注意很多细节：
    
    1、可重入锁
    
    2、自动续约
    
    3、唯一ID解锁
    
    4、设置超时时间避免永久锁
    
    5、加锁、解锁的原子性操作
    
    5、适配redis多种集群模式
    
    针对以上5点基于redis这里推荐使用redisson，不仅默认支持原子操作，同时也支持自动续约，另外，Redisson 还提供了对 Redlock 算法的支持；
    
## 如何优雅的使用分布式锁

    1、常见的分布式锁代码如下：
    
    try {
        lock = redisClient.tryLock(key, true, 30, 10);
        if (lock) {
            ......
        }
    } catch (Exception e) {
        log.error("acquire task error!", e);
    } finally {
        if (lock) {
            try {
                redisClient.unLock(RedisConstants.REDIS_WORKER_CONSUME_TASK_LOCK);
            } catch (Exception e) {
                log.error("unlock error!", e);
            }
        }
    }
    
    缺点：一个项目中很多函数都有可能用到分布式锁，而函数中的锁使用代码完全一致，造成了大量冗余代码。
    
    2、本案例分布式锁的使用：
    
    @RedisLock(key = "com:haizhi:test:lock:first") // 使用注解即可
    public void processFirst() {
        ......
    }
    
    优点：通过注解 & AOP 实现了只需在需要使用分布式的函数上加上注解即可实现, 开发者只需要关注业务即可, 使用优雅；
         另外通过AOP扫描的方式, 无需开发者自己实现扫描程序, 十分巧妙。
