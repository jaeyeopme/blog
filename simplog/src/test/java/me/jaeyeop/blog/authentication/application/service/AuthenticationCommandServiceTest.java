package me.jaeyeop.blog.authentication.application.service;

import static me.jaeyeop.blog.config.token.JWTProvider.TYPE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import me.jaeyeop.blog.authentication.adapter.in.AuthenticationRequest.Logout;
import me.jaeyeop.blog.authentication.adapter.in.AuthenticationRequest.Refresh;
import me.jaeyeop.blog.support.UnitTestSupport;
import me.jaeyeop.blog.support.helper.TokenProviderHelper;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.BadCredentialsException;

/**
 * @author jaeyeopme Created on 10/02/2022.
 */
class AuthenticationCommandServiceTest extends UnitTestSupport {

  @Test
  void 토큰_만료() {
    final var email = "email@email.com";
    final var accessToken = tokenProvider.createAccess(email).value();
    final var refreshToken = tokenProvider.createRefresh(email).value();
    given(expiredTokenQueryPort.isExpired(accessToken)).willReturn(Boolean.FALSE);
    given(refreshTokenQueryPort.isExpired(refreshToken)).willReturn(Boolean.FALSE);

    final ThrowingCallable when = () -> authCommandService.logout(
        getLogoutRequest(accessToken, refreshToken));

    assertThatNoException().isThrownBy(when);
    then(expiredTokenCommandPort).should().expire(any());
    then(refreshTokenCommandPort).should().expire(any());
  }

  @Test
  void 이미_만료된_엑세스_토큰_만료() {
    final var email = "email@email.com";
    final var accessToken = tokenProvider.createAccess(email).value();
    final var refreshToken = tokenProvider.createRefresh(email).value();
    given(expiredTokenQueryPort.isExpired(accessToken)).willReturn(Boolean.TRUE);

    final ThrowingCallable when = () -> authCommandService.logout(
        getLogoutRequest(accessToken, refreshToken));

    assertThatNoException().isThrownBy(when);
    then(expiredTokenCommandPort).should(never()).expire(any());
    then(refreshTokenCommandPort).should().expire(any());
  }

  @Test
  void 이미_만료된_리프레시_토큰_만료() {
    final var email = "email@email.com";
    final var accessToken = tokenProvider.createAccess(email).value();
    final var refreshToken = tokenProvider.createRefresh(email).value();
    given(expiredTokenQueryPort.isExpired(accessToken)).willReturn(Boolean.FALSE);
    given(refreshTokenQueryPort.isExpired(refreshToken)).willReturn(Boolean.TRUE);

    final ThrowingCallable when = () -> authCommandService.logout(
        getLogoutRequest(accessToken, refreshToken));

    assertThatNoException().isThrownBy(when);
    then(expiredTokenCommandPort).should().expire(any());
    then(refreshTokenCommandPort).should(never()).expire(any());
  }

  @Test
  void 유효하지_않은_엑세스_토큰_만료() {
    final var email = "email@email.com";
    final var expiredProvider = TokenProviderHelper.createExpired();
    final var accessToken = expiredProvider.createAccess(email).value();
    final var refreshToken = tokenProvider.createRefresh(email).value();

    final ThrowingCallable when = () -> authCommandService.logout(
        getLogoutRequest(accessToken, refreshToken));

    assertThatThrownBy(when).isInstanceOf(BadCredentialsException.class);
    then(expiredTokenCommandPort).should(never()).expire(any());
    then(refreshTokenCommandPort).should(never()).expire(any());
  }

  @Test
  void 유효하지_않은_리프레시_토큰_만료() {
    final var email = "email@email.com";
    final var expiredProvider = TokenProviderHelper.createExpired();
    final var accessToken = tokenProvider.createAccess(email).value();
    final var refreshToken = expiredProvider.createRefresh(email).value();

    final ThrowingCallable when = () -> authCommandService.logout(
        getLogoutRequest(accessToken, refreshToken));

    assertThatThrownBy(when).isInstanceOf(BadCredentialsException.class);
    then(expiredTokenCommandPort).should(never()).expire(any());
    then(refreshTokenCommandPort).should(never()).expire(any());
  }

  @Test
  void 리프레시_토큰으로_엑세스_토큰_재발급() {
    final var email = "email@email.com";
    final var accessToken = tokenProvider.createAccess(email);
    final var refreshToken = tokenProvider.createRefresh(email);
    final var newAccessToken = tokenProvider.createAccess(email);
    given(refreshTokenQueryPort.isExpired(refreshToken.value())).willReturn(Boolean.FALSE);

    final var actual = authCommandService.refresh(
        getRefreshRequest(accessToken.value(), refreshToken.value()));

    assertThat(actual).isEqualTo(newAccessToken.value());
    then(expiredTokenCommandPort).should().expire(any());
  }

  @Test
  void 만료된_리프레시_토큰으로_엑세스_토큰_재발급() {
    final var email = "email@email.com";
    final var accessToken = tokenProvider.createAccess(email);
    final var refreshToken = tokenProvider.createRefresh(email);
    given(refreshTokenQueryPort.isExpired(refreshToken.value())).willReturn(Boolean.TRUE);

    final ThrowingCallable when = () -> authCommandService.refresh(
        getRefreshRequest(accessToken.value(), refreshToken.value()));

    assertThatThrownBy(when).isInstanceOf(BadCredentialsException.class);
  }

  private Logout getLogoutRequest(final String accessToken, final String refreshToken) {
    return new Logout(TYPE + accessToken, TYPE + refreshToken);
  }

  private Refresh getRefreshRequest(final String accessToken, final String refreshToken) {
    return new Refresh(TYPE + accessToken, TYPE + refreshToken);
  }

}
