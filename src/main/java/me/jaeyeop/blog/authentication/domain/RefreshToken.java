package me.jaeyeop.blog.authentication.domain;

import java.util.concurrent.TimeUnit;

import jakarta.validation.constraints.NotBlank;

import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@EqualsAndHashCode
@RedisHash("refreshToken")
public class RefreshToken {
    @NotBlank @Id private String value;

    @TimeToLive(unit = TimeUnit.MILLISECONDS)
    private long expirationMillis;

    protected RefreshToken() {}

    private RefreshToken(final String value, final long expirationMillis) {
        this.value = value;
        this.expirationMillis = expirationMillis;
    }

    public static RefreshToken from(Token token) {
        return new RefreshToken(token.value(), token.expiration());
    }
}
