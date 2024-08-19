package com.artli.springbootinit.manager;

import com.google.common.util.concurrent.RateLimiter;
import org.apache.poi.ss.formula.functions.T;

import java.util.concurrent.ConcurrentHashMap;

/**
 *适用于单服务器的限制器
 */
public class GuavaLimiterUtils {

    // 可以考虑使用线程安全的 Map 来存储多个 RateLimiter 实例
    private static final ConcurrentHashMap<Long, RateLimiter> limiters = new ConcurrentHashMap<>();

    /**
     * 获取或创建一个 RateLimiter 实例。
     *
     * @param id           RateLimiter 的名称，用于标识不同的 RateLimiter 实例。
     * @param permitsPerSec  每秒允许的请求数量。
     * @return RateLimiter 实例。
     */
    public static RateLimiter getRateLimiter(Long id, double permitsPerSec) {
        return limiters.computeIfAbsent(id, n -> RateLimiter.create(permitsPerSec));
    }


}
