package me.jaeyeop.blog.support.helper;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import me.jaeyeop.blog.commons.token.JWTProvider;
import me.jaeyeop.blog.commons.token.TokenProvider;

/**
 * @author jaeyeopme Created on 10/02/2022.
 */
public class TokenProviderHelper {

  private static final Clock DEFAULT_JWT_CLOCK = Clock.fixed(Instant.now(), ZoneId.systemDefault());

  private static final String DEFAULT_JWT_KEY = "key-key-key-key-key-key";

  private static final int DEFAULT_JWT_ACCESS_EXP = 200000;

  private static final int DEFAULT_JWT_REFRESH_EXP = 600000;

  public static TokenProvider createDifferentKey() {
    return new JWTProvider(
        "differentKey-differentKey-differentKey",
        DEFAULT_JWT_ACCESS_EXP,
        DEFAULT_JWT_REFRESH_EXP,
        DEFAULT_JWT_CLOCK);
  }

  public static TokenProvider createExpired() {
    return new JWTProvider(
        DEFAULT_JWT_KEY,
        -1000,
        -1000,
        DEFAULT_JWT_CLOCK);
  }

  public static TokenProvider create() {
    return new JWTProvider(
        DEFAULT_JWT_KEY,
        DEFAULT_JWT_ACCESS_EXP,
        DEFAULT_JWT_REFRESH_EXP,
        DEFAULT_JWT_CLOCK);
  }

}
