package com.elearning.elearning_support.services.redis.impl;

import java.util.Objects;
import java.util.concurrent.TimeUnit;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import com.elearning.elearning_support.services.redis.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class RedisServiceImpl implements RedisService {

    private final RedisTemplate<String, Object> redisTemplateDefault;

    private final RedisTemplate<String, String> redisTemplateString;

    @Override
    public boolean isValidRedisTemplate() {
        return Objects.nonNull(redisTemplateDefault);
    }

    @Override
    public Object getObjectValue(String key) {
        return redisTemplateDefault.opsForValue().get(key);
    }

    @Override
    public void putObjectWithExpiration(String key, Object value, Long timeout, TimeUnit unit) {
        redisTemplateDefault.opsForValue().set(key, value, timeout, unit);
    }

    @Override
    public void putStringWithExpiration(String key, String value, Long timeout, TimeUnit unit) {
        redisTemplateString.opsForValue().set(key, value, timeout, unit);
    }

    @Override
    public String getStringValue(String key) {
        return redisTemplateString.opsForValue().get(key);
    }

    @Override
    public void put(String key, Object value) {
        redisTemplateDefault.opsForValue().set(key, value);
    }

    @Override
    public void remove(String key) {
        redisTemplateDefault.delete(key);
    }
}
