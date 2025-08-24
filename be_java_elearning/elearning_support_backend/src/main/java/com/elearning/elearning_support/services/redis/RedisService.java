package com.elearning.elearning_support.services.redis;

import java.util.concurrent.TimeUnit;
import org.springframework.stereotype.Service;

@Service
public interface RedisService {

    Object getObjectValue(String key);

    String getStringValue(String key);

    void putObjectWithExpiration(String key, Object value, Long timeout, TimeUnit unit);

    void putStringWithExpiration(String key, String value, Long timeout, TimeUnit unit);

    void put(String key, Object value);

    void remove(String key);

    boolean isValidRedisTemplate();

}
