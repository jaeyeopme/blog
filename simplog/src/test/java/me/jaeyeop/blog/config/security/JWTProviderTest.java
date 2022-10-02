package me.jaeyeop.blog.config.security;

import static org.assertj.core.api.BDDAssertions.then;
import java.time.Instant;
import org.junit.jupiter.api.Test;

public class JWTProviderTest {

  @Test
  void 엑세스_토큰_발급() {
    final var email = "email@email.com";
    final var jwtProvider = JWTProviderFactory.create();
    final var accessToken = jwtProvider.issueAccessToken(email);

    final var actual = jwtProvider.getEmail(accessToken).orElse(null);

    then(actual).isEqualTo(email);
  }

  @Test
  void 리프레시_토큰_발급() {
    final var email = "email@email.com";
    final var jwtProvider = JWTProviderFactory.create();
    final var refreshToken = jwtProvider.issueRefreshToken(email);

    final var actual = jwtProvider.getEmail(refreshToken).orElse(null);

    then(actual).isEqualTo(email);
  }

  @Test
  void 잘못된_키를_가진_토큰에서_정보_가져오기_실패() {
    final var jwtProvider = JWTProviderFactory.create();
    final var wrongKeyProvider = JWTProviderFactory.createWrongKey();
    final var wrongKeyToken = wrongKeyProvider.issueAccessToken("email@email.com");

    final var actual = jwtProvider.getEmail(wrongKeyToken);

    then(actual).isEmpty();
  }

  @Test
  void 만료된_토큰에서_정보_가져오기_실패() {
    final var jwtProvider = JWTProviderFactory.create();
    final var expiredProvider = JWTProviderFactory.createExpiredProvider();
    final var expiredToken = expiredProvider.issueAccessToken("email@email.com");

    final var actual = jwtProvider.getEmail(expiredToken);

    then(actual).isEmpty();
  }

  @Test
  void 만료되지_않은_토큰_남은_만료시간_조회() {
    final var jwtProvider = JWTProviderFactory.create();
    final var accessToken = jwtProvider.issueAccessToken("email@email.com");
    final var instant = Instant.now();

    final var actual = jwtProvider.getRemaining(accessToken, instant);

    then(actual).isPositive();
  }


  @Test
  void 만료된_토큰_남은_만료시간_조회() {
    final var jwtProvider = JWTProviderFactory.createExpiredProvider();
    final var accessToken = jwtProvider.issueAccessToken("email@email.com");
    final var instant = Instant.now();

    then(jwtProvider.getRemaining(accessToken, instant)).isZero();
  }

}