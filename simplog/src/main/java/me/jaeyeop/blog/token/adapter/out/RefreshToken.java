package me.jaeyeop.blog.token.adapter.out;

import java.util.concurrent.TimeUnit;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@Getter
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

