package me.jaeyeop.blog.token.domain;

import java.time.Instant;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class Token {

  private final String value;

  private final String email;

  private final long expiration;

  public Token(final String value,
      final String email,
      final long expiration) {
    this.value = value;
    this.email = email;
    this.expiration = expiration;
  }

  public long getRemaining() {
    return Instant.now().toEpochMilli() - expiration;
  }

}
