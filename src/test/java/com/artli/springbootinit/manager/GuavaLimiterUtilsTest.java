package com.artli.springbootinit.manager;

import com.artli.springbootinit.common.ErrorCode;
import com.artli.springbootinit.exception.BusinessException;
import com.google.common.util.concurrent.RateLimiter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GuavaLimiterUtilsTest {



    @Test
    void tryAcquire() {


        Long id = 123131L;
        RateLimiter limiter = GuavaLimiterUtils.getRateLimiter(id, 2.0);

        while (true){
            boolean b = limiter.tryAcquire(1);
            if (b) {
                System.out.println("成功");
            }else {
                throw new BusinessException(ErrorCode.MANY_REQUEST,"请勿频繁点击");
            }
        }


    }
}