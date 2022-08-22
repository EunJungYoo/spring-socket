package com.example.springsocket.config;

import io.lettuce.core.dynamic.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import redis.embedded.RedisServer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
/**
 * 로컬 환경일경우 내장 레디스가 실행됩니다.
 * 스프링이 실행됨과 동시에 embedded redis 서버도 동시 실행
 */
@Profile("local")
@Configuration
public class EmbeddedRedisConfig {

/*    @Value("${spring.redis.port}")*/
    private int redisPort = 6379;

    private RedisServer redisServer;

    @PostConstruct
    public void redisServer() {
        redisServer = new RedisServer(redisPort);
        redisServer.start();
    }

    @PreDestroy
    public void stopRedis() {
        if (redisServer != null) {
            redisServer.stop();
        }
    }
}