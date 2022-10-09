package me.jaeyeop.blog.token.domain;

import java.time.Instant;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@EqualsAndHashCode
@RequiredArgsConstructor
public class Token {

  private final String value;

  private final String email;

  private final long expiration;

  public long getRemaining() {
    return Instant.now().toEpochMilli() - expiration;
  }

}
