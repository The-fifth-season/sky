package com.sky.config;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sky.utils.RandomUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

//官方文档地址：https://docs.spring.io/spring-data/redis/reference/redis/redis-cache.html#redis:support:cache-abstraction:expiration:tti2
@Configuration
public class CacheConfiguration {
    @Bean
    @Primary        //默认策略
    public RedisCacheManager cacheManager1Hour (RedisConnectionFactory redisConnectionFactory){
        RedisCacheConfiguration redisCacheConfiguration = cacheConfiguration(3600L+RandomUtil.getRandomLong(1L,30L));
        return RedisCacheManager.builder(redisConnectionFactory)
                .cacheDefaults(redisCacheConfiguration)
                .transactionAware()
                .build();
    }
    @Bean
    public RedisCacheManager cacheManager1Minute (RedisConnectionFactory redisConnectionFactory){
        RedisCacheConfiguration redisCacheConfiguration = cacheConfiguration(60L+RandomUtil.getRandomLong(1L,30L));
        return RedisCacheManager.builder(redisConnectionFactory)
                .cacheDefaults(redisCacheConfiguration)
                .transactionAware()
                .build();
    }
    @Bean
    public RedisCacheManager cacheManager1Day (RedisConnectionFactory redisConnectionFactory){
        RedisCacheConfiguration redisCacheConfiguration = cacheConfiguration(60L+RandomUtil.getRandomLong(1L,30L));
        return RedisCacheManager.builder(redisConnectionFactory)
                .cacheDefaults(redisCacheConfiguration)
                .transactionAware()
                .build();
    }

    @Bean
    public RedisCacheManager cacheManagerForever(RedisConnectionFactory redisConnectionFactory){
        RedisCacheConfiguration redisCacheConfiguration = cacheConfiguration(-1L);
        return RedisCacheManager.builder(redisConnectionFactory)
                .cacheDefaults(redisCacheConfiguration)
                .transactionAware()
                .build();
    }
    private RedisCacheConfiguration cacheConfiguration(Long ttl) {
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(SerializationFeature.WRITE_DURATIONS_AS_TIMESTAMPS);
        objectMapper.registerModule(new JavaTimeModule());
        //只针对非空值进行序列化
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        //将类型序列化为属性json字符串
        objectMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance,
                ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);

        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);

        return RedisCacheConfiguration.defaultCacheConfig()
                // 设置缓存过期时间,+随机数，避免缓存雪崩
                .entryTtl(Duration.ofSeconds(ttl))
                //不缓存空值
                .disableCachingNullValues()
                // 设置序列化器
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(jackson2JsonRedisSerializer));
    }
}
