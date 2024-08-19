package com.artli.springbootinit.config;

import lombok.Data;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 分布式限流
 */
@Configuration
//prefix与配置类文件中的redis对应
@ConfigurationProperties(prefix = "spring.redis")
@Data
public class RedissonConfig {

    @Bean
    public RedissonClient redissonClient(){
        Config config = new Config();
        config.useSingleServer()
                .setAddress("redis://192.168.115.146:6379")
                .setDatabase(1);
        RedissonClient redissonClient = Redisson.create(config);
        return redissonClient;

    }
}
