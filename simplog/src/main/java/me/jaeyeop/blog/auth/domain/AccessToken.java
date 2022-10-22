package me.jaeyeop.blog.auth.domain;

import java.util.concurrent.TimeUnit;
import javax.validation.constraints.NotBlank;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@EqualsAndHashCode
@RedisHash("accessToken")
public class AccessToken {

  @NotBlank
  @Id
  private String value;

  @TimeToLive(unit = TimeUnit.MILLISECONDS)
  private long remaining;

  protected AccessToken() {
  }

  public AccessToken(final String value,
      final long remaining) {
    this.value = value;
    this.remaining = remaining;
  }

}
