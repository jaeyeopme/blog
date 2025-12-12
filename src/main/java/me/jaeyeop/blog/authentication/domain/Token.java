package me.jaeyeop.blog.authentication.domain;

import java.time.Instant;

public record Token(String value,
                    String email,
                    long expiration) {

  public long remaining() {
    return expiration - Instant.now().toEpochMilli();
  }

}
