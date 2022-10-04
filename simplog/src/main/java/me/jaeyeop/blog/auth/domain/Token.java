package me.jaeyeop.blog.auth.domain;

import java.time.Instant;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Token {

  private final String value;

  private final String email;

  private final long expiration;

  public long getRemaining() {
    return Instant.now().toEpochMilli() - expiration;
  }

}
