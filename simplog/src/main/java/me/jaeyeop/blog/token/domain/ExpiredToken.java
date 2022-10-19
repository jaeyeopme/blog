package me.jaeyeop.blog.token.domain;

import java.util.concurrent.TimeUnit;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@Getter
@RedisHash("expiredToken")
public class ExpiredToken {

  @NotBlank
  @Id
  private String value;

  @TimeToLive(unit = TimeUnit.MILLISECONDS)
  private long remaining;

  protected ExpiredToken() {
  }

  public ExpiredToken(final String value,
      final long remaining) {
    this.value = value;
    this.remaining = remaining;
  }

}
