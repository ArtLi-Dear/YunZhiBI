package com.artli.springbootinit.manager;


import com.artli.springbootinit.common.ErrorCode;
import com.artli.springbootinit.exception.ThrowUtils;
import org.redisson.Redisson;
import org.redisson.api.RRateLimiter;
import org.redisson.api.RateIntervalUnit;
import org.redisson.api.RateType;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


/**
 * 通用分布式限流器
 */
@Service
public class RedisLimitManager {

    @Resource
    private RedissonClient redissonClient;

    //区分不同限流器 :用户ID
    public void doRateLimit(String key){

        //创建一个限流器
        //每秒请求两次
        RRateLimiter rateLimiter = redissonClient.getRateLimiter(key);
        rateLimiter.trySetRate(RateType.OVERALL,2, 1,RateIntervalUnit.SECONDS);

        //请求一个令牌
        boolean tryAcquire = rateLimiter.tryAcquire(1);
        ThrowUtils.throwIf(!tryAcquire, ErrorCode.MANY_REQUEST,"请勿频繁请求!");

    }

}
