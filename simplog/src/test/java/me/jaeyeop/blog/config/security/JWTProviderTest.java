package me.jaeyeop.blog.config.security;

import static org.assertj.core.api.Assertions.assertThat;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import me.jaeyeop.blog.user.domain.UserFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JWTProviderTest {

  private static final String DEFAULT_KEY = "key-key-key-key-key-key";

  private static final int DEFAULT_EXPIRATION_ACCESS = 200000;

  private static final int DEFAULT_EXPIRATION_REFRESH = 600000;

  private static final Clock DEFAULT_CLOCK = Clock.fixed(Instant.now(), ZoneId.systemDefault());

  private JWTProvider jwtProvider;

  @BeforeEach
  void setUp() {
    jwtProvider = new JWTProvider(DEFAULT_CLOCK,
        DEFAULT_KEY,
        DEFAULT_EXPIRATION_ACCESS,
        DEFAULT_EXPIRATION_REFRESH);
  }

  @Test
  void 엑세스_토큰_발급() {
    final var user = UserFactory.create();
    final var accessToken = jwtProvider.issueAccessToken(user.getEmail());

    final var actualEmail = jwtProvider.getEmail(accessToken).orElse(null);
    assertThat(actualEmail).isEqualTo(user.getEmail());
  }

  @Test
  void 리프레시_토큰_발급() {
    final var user = UserFactory.create();
    final var refreshToken = jwtProvider.issueRefreshToken(user.getEmail());

    final var actualEmail = jwtProvider.getEmail(refreshToken).orElse(null);
    assertThat(actualEmail).isEqualTo(user.getEmail());
  }

  @Test
  void 잘못된_키를_가진_토큰에서_정보_가져오기_실패() {
    final var user = UserFactory.create();
    final var wrongKeyProvider = createWrongKeyProvider();
    final var wrongKeyToken = wrongKeyProvider.issueAccessToken(user.getEmail());

    final var empty = jwtProvider.getEmail(wrongKeyToken);

    assertThat(empty).isEmpty();
  }

  @Test
  void 만료된_토큰에서_정보_가져오기_실패() {
    final var user = UserFactory.create();
    final var expiredProvider = createExpiredProvider();
    final var expiredToken = expiredProvider.issueAccessToken(user.getEmail());

    final var empty = expiredProvider.getEmail(expiredToken);

    assertThat(empty).isEmpty();
  }

  private JWTProvider createWrongKeyProvider() {
    return new JWTProvider(DEFAULT_CLOCK,
        "wrongKey-wrongKey-wrongKey",
        DEFAULT_EXPIRATION_ACCESS,
        DEFAULT_EXPIRATION_REFRESH);
  }

  private JWTProvider createExpiredProvider() {
    return new JWTProvider(DEFAULT_CLOCK,
        DEFAULT_KEY,
        -1000,
        DEFAULT_EXPIRATION_REFRESH);
  }

}