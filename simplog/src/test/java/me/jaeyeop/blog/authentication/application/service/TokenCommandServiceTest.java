package me.jaeyeop.blog.authentication.application.service;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import me.jaeyeop.blog.authentication.adapter.in.AuthenticationRequest.Expire;
import me.jaeyeop.blog.authentication.application.port.out.AccessTokenCommandPort;
import me.jaeyeop.blog.authentication.application.port.out.AccessTokenQueryPort;
import me.jaeyeop.blog.authentication.application.port.out.RefreshTokenCommandPort;
import me.jaeyeop.blog.authentication.application.port.out.RefreshTokenQueryPort;
import me.jaeyeop.blog.config.token.JWTProvider;
import me.jaeyeop.blog.config.token.TokenProvider;
import me.jaeyeop.blog.config.token.TokenProviderFactory;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;

@ExtendWith(MockitoExtension.class)
class TokenCommandServiceTest {

  @Mock
  private AccessTokenCommandPort accessTokenCommandPort;

  @Mock(stubOnly = true)
  private AccessTokenQueryPort accessTokenQueryPort;

  @Mock
  private RefreshTokenCommandPort refreshTokenCommandPort;

  @Mock(stubOnly = true)
  private RefreshTokenQueryPort refreshTokenQueryPort;

  @Spy
  private TokenProvider tokenProvider = TokenProviderFactory.createDefault();

  @InjectMocks
  private AuthenticationCommandService authCommandService;

  @Test
  void 토큰_만료() {
    final var email = "email@email.com";
    final var accessToken = tokenProvider.createAccess(email).value();
    final var refreshToken = tokenProvider.createRefresh(email).value();
    given(accessTokenQueryPort.isExpired(accessToken)).willReturn(Boolean.FALSE);
    given(refreshTokenQueryPort.isExpired(refreshToken)).willReturn(Boolean.FALSE);

    final ThrowingCallable when = () -> authCommandService.expire(
        getExpire(accessToken, refreshToken));

    assertThatNoException().isThrownBy(when);
    then(accessTokenCommandPort).should().expire(any());
    then(refreshTokenCommandPort).should().expire(any());
  }

  @Test
  void 이미_만료된_엑세스_토큰을_만료() {
    final var email = "email@email.com";
    final var accessToken = tokenProvider.createAccess(email).value();
    final var refreshToken = tokenProvider.createRefresh(email).value();
    given(accessTokenQueryPort.isExpired(accessToken)).willReturn(Boolean.TRUE);

    final ThrowingCallable when = () -> authCommandService.expire(
        getExpire(accessToken, refreshToken));

    assertThatNoException().isThrownBy(when);
    then(accessTokenCommandPort).should(never()).expire(any());
    then(refreshTokenCommandPort).should().expire(any());
  }

  @Test
  void 이미_만료된_리프레시_토큰을_만료() {
    final var email = "email@email.com";
    final var accessToken = tokenProvider.createAccess(email).value();
    final var refreshToken = tokenProvider.createRefresh(email).value();
    given(accessTokenQueryPort.isExpired(accessToken)).willReturn(Boolean.FALSE);
    given(refreshTokenQueryPort.isExpired(refreshToken)).willReturn(Boolean.TRUE);

    final ThrowingCallable when = () -> authCommandService.expire(
        getExpire(accessToken, refreshToken));

    assertThatNoException().isThrownBy(when);
    then(accessTokenCommandPort).should().expire(any());
    then(refreshTokenCommandPort).should(never()).expire(any());
  }

  @Test
  void 유효하지_않은_엑세스_토큰_만료() {
    final var email = "email@email.com";
    final var expiredProvider = TokenProviderFactory.createExpired();
    final var accessToken = expiredProvider.createAccess(email).value();
    final var refreshToken = tokenProvider.createRefresh(email).value();

    final ThrowingCallable when = () -> authCommandService.expire(
        getExpire(accessToken, refreshToken));

    assertThatThrownBy(when).isInstanceOf(BadCredentialsException.class);
    then(accessTokenCommandPort).should(never()).expire(any());
    then(refreshTokenCommandPort).should(never()).expire(any());
  }

  @Test
  void 유효하지_않은_리프레시_토큰_만료() {
    final var email = "email@email.com";
    final var expiredProvider = TokenProviderFactory.createExpired();
    final var accessToken = tokenProvider.createAccess(email).value();
    final var refreshToken = expiredProvider.createRefresh(email).value();

    final ThrowingCallable when = () -> authCommandService.expire(
        getExpire(accessToken, refreshToken));

    assertThatThrownBy(when).isInstanceOf(BadCredentialsException.class);
    then(accessTokenCommandPort).should(never()).expire(any());
    then(refreshTokenCommandPort).should(never()).expire(any());
  }

  private Expire getExpire(final String accessToken, final String refreshToken) {
    return new Expire(JWTProvider.TYPE + accessToken, JWTProvider.TYPE + refreshToken);
  }

}