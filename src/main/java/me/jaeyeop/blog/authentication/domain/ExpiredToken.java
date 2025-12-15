package me.jaeyeop.blog.authentication.domain;

import jakarta.validation.constraints.NotBlank;
import java.util.concurrent.TimeUnit;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@EqualsAndHashCode
@RedisHash("expiredToken")
public class ExpiredToken {

    @NotBlank @Id private String value;

    @TimeToLive(unit = TimeUnit.MILLISECONDS)
    private long remaining;

    protected ExpiredToken() {}

    private ExpiredToken(final String value, final long remaining) {
        this.value = value;
        this.remaining = remaining;
    }

    public static ExpiredToken from(final Token token) {
        return new ExpiredToken(token.value(), token.remaining());
    }
}
