package me.jaeyeop.blog.config.security;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import me.jaeyeop.blog.auth.application.port.in.TokenProvideUseCase;
import me.jaeyeop.blog.auth.application.service.TokenProvideService;

public class JWTProvideServiceFactory {

  public static final Clock DEFAULT_CLOCK = Clock.fixed(Instant.now(), ZoneId.systemDefault());

  private static final String DEFAULT_KEY = "key-key-key-key-key-key";

  private static final int DEFAULT_EXPIRATION_ACCESS = 200000;

  private static final int DEFAULT_EXPIRATION_REFRESH = 600000;

  public static TokenProvideUseCase createWrongKey() {
    return new TokenProvideService(
        "wrongKey-wrongKey-wrongKey",
        DEFAULT_EXPIRATION_ACCESS,
        DEFAULT_EXPIRATION_REFRESH,
        DEFAULT_CLOCK);
  }

  public static TokenProvideUseCase createExpired() {
    return new TokenProvideService(
        DEFAULT_KEY,
        -1000,
        -1000,
        DEFAULT_CLOCK);
  }

  public static TokenProvideUseCase create() {
    return new TokenProvideService(
        DEFAULT_KEY,
        DEFAULT_EXPIRATION_ACCESS,
        DEFAULT_EXPIRATION_REFRESH,
        DEFAULT_CLOCK);
  }

}
