package me.jaeyeop.blog.config.security;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

public class JWTProviderFactory {

  public static final Clock DEFAULT_CLOCK = Clock.fixed(Instant.now(), ZoneId.systemDefault());
  private static final String DEFAULT_KEY = "key-key-key-key-key-key";
  private static final int DEFAULT_EXPIRATION_ACCESS = 200000;
  private static final int DEFAULT_EXPIRATION_REFRESH = 600000;

  public static JWTProvider createWrongKey() {
    return new JWTProvider(DEFAULT_CLOCK,
        "wrongKey-wrongKey-wrongKey",
        DEFAULT_EXPIRATION_ACCESS,
        DEFAULT_EXPIRATION_REFRESH);
  }

  public static JWTProvider createExpiredProvider() {
    return new JWTProvider(DEFAULT_CLOCK,
        DEFAULT_KEY,
        -1000,
        DEFAULT_EXPIRATION_REFRESH);
  }

  public static JWTProvider create() {
    return new JWTProvider(DEFAULT_CLOCK,
        DEFAULT_KEY,
        DEFAULT_EXPIRATION_ACCESS,
        DEFAULT_EXPIRATION_REFRESH);
  }

}
