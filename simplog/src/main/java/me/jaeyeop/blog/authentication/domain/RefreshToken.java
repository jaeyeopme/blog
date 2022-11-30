package me.jaeyeop.blog.authentication.domain;

import java.util.concurrent.TimeUnit;
import javax.validation.constraints.NotBlank;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@EqualsAndHashCode
@RedisHash("refreshToken")
public class RefreshToken {

  @NotBlank
  @Id
  private String value;

  @TimeToLive(unit = TimeUnit.MILLISECONDS)
  private long expiration;

  protected RefreshToken() {
  }

  public RefreshToken(final String value,
      final long expiration) {
    this.value = value;
    this.expiration = expiration;
  }

}

