package me.jaeyeop.blog.config.token;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenThrownBy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.BadCredentialsException;

class TokenProviderTest {

  private TokenProvider tokenProvider;

  @BeforeEach
  void setUp() {
    tokenProvider = TokenProviderFactory.createDefault();
  }

  @Test
  void 엑세스_토큰_발급() {
    final var email = "email@email.com";

    final var accessToken = tokenProvider.createAccess(email);

    then(accessToken.getEmail()).isEqualTo(email);
  }

  @Test
  void 리프레시_토큰_발급() {
    final var email = "email@email.com";

    final var refreshToken = tokenProvider.createRefresh(email);

    then(refreshToken.getEmail()).isEqualTo(email);
  }

  @Test
  void 다른_키를_가진_토큰_검증() {
    final var differentKeyProvider = TokenProviderFactory.createDifferentKey();
    final var accessToken = differentKeyProvider.createAccess("email@email.com");

    thenThrownBy(() -> tokenProvider.authenticate(accessToken.getValue()))
        .isInstanceOf(BadCredentialsException.class);
  }

  @Test
  void 만료된_토큰_검증() {
    final var expiredProvider = TokenProviderFactory.createExpired();
    final var accessToken = expiredProvider.createAccess("email@email.com");

    thenThrownBy(() -> tokenProvider.authenticate(accessToken.getValue()))
        .isInstanceOf(BadCredentialsException.class);
  }

}