package me.jaeyeop.blog.config.security;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import me.jaeyeop.blog.config.token.JWTProvider;
import me.jaeyeop.blog.config.token.TokenProvider;

public class JWTProviderFactory {

  public static final Clock DEFAULT_CLOCK = Clock.fixed(Instant.now(), ZoneId.systemDefault());

  private static final String DEFAULT_KEY = "key-key-key-key-key-key";

  private static final int DEFAULT_EXPIRATION_ACCESS = 200000;

  private static final int DEFAULT_EXPIRATION_REFRESH = 600000;

  public static TokenProvider createWrongKey() {
    return new JWTProvider(
        "wrongKey-wrongKey-wrongKey",
        DEFAULT_EXPIRATION_ACCESS,
        DEFAULT_EXPIRATION_REFRESH,
        DEFAULT_CLOCK);
  }

  public static TokenProvider createExpired() {
    return new JWTProvider(
        DEFAULT_KEY,
        -1000,
        -1000,
        DEFAULT_CLOCK);
  }

  public static TokenProvider create() {
    return new JWTProvider(
        DEFAULT_KEY,
        DEFAULT_EXPIRATION_ACCESS,
        DEFAULT_EXPIRATION_REFRESH,
        DEFAULT_CLOCK);
  }

}
