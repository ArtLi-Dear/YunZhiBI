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
     * @param name           RateLimiter 的名称，用于标识不同的 RateLimiter 实例。
     * @param permitsPerSec  每秒允许的请求数量。
     * @return RateLimiter 实例。
     */
    public static RateLimiter getRateLimiter(Long id, double permitsPerSec) {
        return limiters.computeIfAbsent(id, n -> RateLimiter.create(permitsPerSec));
    }

    /**
     * 尝试获取令牌。
     *
     * @param id RateLimiter 的名称。
     * @return 是否成功获取到令牌。
     */
    public static boolean tryAcquire(Long id) {
        RateLimiter rateLimiter = limiters.get(id);
        if (rateLimiter != null) {
            return rateLimiter.tryAcquire();
        }
        return false;
    }
}
