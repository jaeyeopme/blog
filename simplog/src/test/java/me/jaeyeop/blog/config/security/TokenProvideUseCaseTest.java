package me.jaeyeop.blog.config.security;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenThrownBy;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.BadCredentialsException;

class TokenProvideUseCaseTest {

  @Test
  void 엑세스_토큰_발급() {
    final var email = "email@email.com";
    final var jwtProvider = JWTProvideServiceFactory.create();
    final var access = jwtProvider.createAccess(email);

    final var actual = jwtProvider.authenticate(access.getValue());

    then(actual.getEmail()).isEqualTo(email);
  }

  @Test
  void 리프레시_토큰_발급() {
    final var email = "email@email.com";
    final var jwtProvider = JWTProvideServiceFactory.create();
    final var refresh = jwtProvider.createRefresh(email);

    final var actual = jwtProvider.authenticate(refresh.getValue());

    then(actual.getEmail()).isEqualTo(email);
  }

  @Test
  void 잘못된_키를_가진_엑세스_토큰_검증_실패() {
    final var jwtProvider = JWTProvideServiceFactory.create();
    final var wrongKeyProvider = JWTProvideServiceFactory.createWrongKey();
    final var access = wrongKeyProvider.createAccess("email@email.com");

    thenThrownBy(() -> jwtProvider.authenticate(access.getValue()))
        .isInstanceOf(BadCredentialsException.class);
  }

  @Test
  void 만료된_토큰_검증_실패() {
    final var jwtProvider = JWTProvideServiceFactory.create();
    final var expiredProvider = JWTProvideServiceFactory.createExpired();
    final var access = expiredProvider.createAccess("email@email.com");

    thenThrownBy(() -> jwtProvider.authenticate(access.getValue()))
        .isInstanceOf(BadCredentialsException.class);
  }

}