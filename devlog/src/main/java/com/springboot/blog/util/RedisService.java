package com.springboot.blog.util;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class RedisService {

    private final StringRedisTemplate stringRedisTemplate;

    public RedisService(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    public String getData(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    public void setDataExpire(String key, String value, Long duration) {
        stringRedisTemplate.opsForValue().set(key, value, Duration.ofMinutes(duration));
    }

    public void deleteData(String key) {
        stringRedisTemplate.delete(key);
    }

}
