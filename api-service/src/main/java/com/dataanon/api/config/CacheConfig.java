package com.dataanon.api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.Map;

@Configuration
@EnableCaching
public class CacheConfig {

    @Value("${cache.ttl.benchmarks:3600000}")
    private long benchmarksTtlMs;

    @Value("${cache.ttl.metrics:86400000}")
    private long metricsTtlMs;

    @Value("${cache.ttl.sectors:86400000}")
    private long sectorsTtlMs;

    @Value("${cache.ttl.dashboard:300000}")
    private long dashboardTtlMs;

    @Value("${cache.ttl.goals:60000}")
    private long goalsTtlMs;

    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        GenericJackson2JsonRedisSerializer jsonSerializer = new GenericJackson2JsonRedisSerializer();

        RedisCacheConfiguration defaults = RedisCacheConfiguration.defaultCacheConfig()
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(jsonSerializer))
                .disableCachingNullValues();

        Map<String, RedisCacheConfiguration> cacheConfigs = Map.of(
                CacheNames.BENCHMARKS, defaults.entryTtl(Duration.ofMillis(benchmarksTtlMs)),
                CacheNames.METRICS,    defaults.entryTtl(Duration.ofMillis(metricsTtlMs)),
                CacheNames.SECTORS,    defaults.entryTtl(Duration.ofMillis(sectorsTtlMs)),
                CacheNames.DASHBOARD,  defaults.entryTtl(Duration.ofMillis(dashboardTtlMs)),
                CacheNames.GOALS,      defaults.entryTtl(Duration.ofMillis(goalsTtlMs))
        );

        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(defaults)
                .withInitialCacheConfigurations(cacheConfigs)
                .build();
    }
}
