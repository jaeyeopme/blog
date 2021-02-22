package com.springboot.blog.util;

import io.lettuce.core.RedisException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;

import static com.springboot.blog.config.SecurityConfig.INVALID_TOKEN_MESSAGE;

@Service
public class RedisService {

    private final StringRedisTemplate stringRedisTemplate;

    public RedisService(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    public String getEmailFromToken(String token) {
        try {
            String email = stringRedisTemplate.opsForValue().get(token);
            deleteToken(token);

            return email;
        } catch (RedisException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, INVALID_TOKEN_MESSAGE);
        }
    }

    public void setToken(String token, String email, Long expirationTime) {
        stringRedisTemplate.opsForValue().set(token, email, Duration.ofMillis(expirationTime));
    }

    public void deleteToken(String token) {
        stringRedisTemplate.delete(token);
    }

}
