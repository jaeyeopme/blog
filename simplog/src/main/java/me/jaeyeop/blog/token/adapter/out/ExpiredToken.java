package me.jaeyeop.blog.token.adapter.out;

import java.util.concurrent.TimeUnit;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@Getter
@RedisHash("expiredToken")
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ExpiredToken {

  @Id
  private String value;

  @TimeToLive(unit = TimeUnit.MILLISECONDS)
  private long remaining;

}
