package me.jaeyeop.blog.auth.adapter.out;

import java.util.concurrent.TimeUnit;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.jaeyeop.blog.auth.domain.Token;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@Getter
@RedisHash("refreshToken")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken {

  @Id
  private String value;

  @TimeToLive(unit = TimeUnit.MILLISECONDS)
  private long expiration;

  public static RefreshToken from(final Token token) {
    return new RefreshToken(token.getValue(), token.getExpiration());
  }

}

