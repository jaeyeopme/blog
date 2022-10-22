package me.jaeyeop.blog.auth.domain;

import java.time.Instant;

public record Token(String value,
                    String email,
                    long expiration) {

  public long remaining() {
    return expiration - Instant.now().toEpochMilli();
  }

}
