package me.jaeyeop.blog.authentication.domain;

import jakarta.validation.constraints.NotBlank;
import java.util.concurrent.TimeUnit;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

/**
 * @author jaeyeopme Created on 10/02/2022.
 */
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

  private RefreshToken(final String value,
      final long expiration) {
    this.value = value;
    this.expiration = expiration;
  }

  public static RefreshToken from(Token token) {
    return new RefreshToken(token.value(), token.expiration());
  }

}

