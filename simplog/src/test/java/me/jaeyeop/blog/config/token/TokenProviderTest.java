package me.jaeyeop.blog.config.token;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenThrownBy;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.BadCredentialsException;

class TokenProviderTest {

  @Test
  void 엑세스_토큰_발급() {
    final var email = "email@email.com";
    final var provider = JWTProviderFactory.createDefault();

    final var token = provider.createAccess(email);

    then(token.getEmail()).isEqualTo(email);
  }

  @Test
  void 리프레시_토큰_발급() {
    final var email = "email@email.com";
    final var provider = JWTProviderFactory.createDefault();

    final var token = provider.createRefresh(email);

    then(token.getEmail()).isEqualTo(email);
  }

  @Test
  void 잘못된_키를_가진_엑세스_토큰_검증_실패() {
    final var provider = JWTProviderFactory.createDefault();
    final var wrongKeyProvider = JWTProviderFactory.createWrongKey();
    final var wrongKeyToken = wrongKeyProvider.createAccess("email@email.com");

    thenThrownBy(() -> provider.authenticate(wrongKeyToken.getValue()))
        .isInstanceOf(BadCredentialsException.class);
  }

  @Test
  void 만료된_토큰_검증_실패() {
    final var provider = JWTProviderFactory.createDefault();
    final var expiredProvider = JWTProviderFactory.createExpired();
    final var expiredToken = expiredProvider.createAccess("email@email.com");

    thenThrownBy(() -> provider.authenticate(expiredToken.getValue()))
        .isInstanceOf(BadCredentialsException.class);
  }

}