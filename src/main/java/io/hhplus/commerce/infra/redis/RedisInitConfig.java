package io.hhplus.commerce.infra.redis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class RedisInitConfig {
    private final CacheManager cacheManager;

    @Bean
    public ApplicationRunner clearCacheOnStartup() {
        return args -> {
            if (cacheManager.getCache("redisCache") != null) {
                cacheManager.getCache("redisCache").clear();
                log.info("redisCache cleared");
            }
        };
    }
}
