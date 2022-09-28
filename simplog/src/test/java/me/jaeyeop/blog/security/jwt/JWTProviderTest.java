package me.jaeyeop.blog.security.jwt;

import static org.assertj.core.api.Assertions.assertThat;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import me.jaeyeop.blog.security.authentication.UserAuthenticationToken;
import me.jaeyeop.blog.user.domain.UserFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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

  @DisplayName("엑세스 토큰을 발급한다.")
  @Test
  void Issue_access_token() {
    final var user = UserFactory.create();
    final var authentication = UserAuthenticationToken
        .authenticated(user.getEmail(), user.getRole());

    final var accessToken = jwtProvider.issueAccessToken(authentication);

    final var actualEmail = jwtProvider.getEmail(accessToken).orElse(null);
    assertThat(actualEmail).isEqualTo(user.getEmail());
  }

  @DisplayName("리프레시 토큰을 발급한다.")
  @Test
  void Issue_refresh_token() {
    final var user = UserFactory.create();
    final var authentication = UserAuthenticationToken
        .authenticated(user.getEmail(), user.getRole());

    final var refreshToken = jwtProvider.issueRefreshToken(authentication);

    final var actualEmail = jwtProvider.getEmail(refreshToken).orElse(null);
    assertThat(actualEmail).isEqualTo(user.getEmail());
  }

  @DisplayName("잘못된 키를 가진 토큰에서 이메일 가져오지 못한다.")
  @Test
  void Get_email_from_token_with_wrong_key() {
    final var authentication = UserFactory.createAuthenticatedToken();
    final var wrongKeyProvider = createWrongKeyProvider();
    final var wrongKeyToken = wrongKeyProvider.issueAccessToken(authentication);

    final var empty = jwtProvider.getEmail(wrongKeyToken);

    assertThat(empty).isEmpty();
  }

  @DisplayName("만료된 토큰에서 이메일을 가져오지 못한다.")
  @Test
  void Get_email_from_expired_token() {
    final var authentication = UserFactory.createAuthenticatedToken();
    final var expiredProvider = createExpiredProvider();
    final var expiredToken = expiredProvider.issueAccessToken(authentication);

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