package me.jaeyeop.blog.unit.authentication;

import static me.jaeyeop.blog.commons.token.JWTProvider.TYPE;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

import me.jaeyeop.blog.authentication.application.port.in.AuthenticationCommandUseCase.LogoutCommand;
import me.jaeyeop.blog.authentication.application.port.in.AuthenticationCommandUseCase.RefreshCommand;
import me.jaeyeop.blog.support.UnitTest;
import me.jaeyeop.blog.support.helper.TokenProviderHelper;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.BadCredentialsException;

class AuthenticationCommandServiceTest extends UnitTest {

    @Test
    void 토큰_만료() {
        // GIVEN
        final var email = "email@email.com";
        final var accessToken = tokenProvider.createAccess(email).value();
        final var refreshToken = tokenProvider.createRefresh(email).value();
        given(expiredTokenQueryPort.isExpired(accessToken)).willReturn(Boolean.FALSE);
        given(refreshTokenQueryPort.isExpired(refreshToken)).willReturn(Boolean.FALSE);
        final var command = new LogoutCommand(TYPE + accessToken, TYPE + refreshToken);

        // WHEN
        authCommandService.logout(command);

        // THEN
        then(expiredTokenCommandPort).should().expire(any());
        then(refreshTokenCommandPort).should().expire(any());
    }

    @Test
    void 이미_만료된_엑세스_토큰_만료() {
        // GIVEN
        final var email = "email@email.com";
        final var accessToken = tokenProvider.createAccess(email).value();
        final var refreshToken = tokenProvider.createRefresh(email).value();
        given(expiredTokenQueryPort.isExpired(accessToken)).willReturn(Boolean.TRUE);
        final var command = new LogoutCommand(TYPE + accessToken, TYPE + refreshToken);

        // WHEN
        authCommandService.logout(command);

        // THEN
        then(expiredTokenCommandPort).should(never()).expire(any());
        then(refreshTokenCommandPort).should().expire(any());
    }

    @Test
    void 이미_만료된_리프레시_토큰_만료() {
        // GIVEN
        final var email = "email@email.com";
        final var accessToken = tokenProvider.createAccess(email).value();
        final var refreshToken = tokenProvider.createRefresh(email).value();
        given(expiredTokenQueryPort.isExpired(accessToken)).willReturn(Boolean.FALSE);
        given(refreshTokenQueryPort.isExpired(refreshToken)).willReturn(Boolean.TRUE);
        final var command = new LogoutCommand(TYPE + accessToken, TYPE + refreshToken);

        // WHEN
        authCommandService.logout(command);

        // THEN
        then(expiredTokenCommandPort).should().expire(any());
        then(refreshTokenCommandPort).should(never()).expire(any());
    }

    @Test
    void 유효하지_않은_엑세스_토큰_만료() {
        // GIVEN
        final var email = "email@email.com";
        final var expiredProvider = TokenProviderHelper.createExpired();
        final var accessToken = expiredProvider.createAccess(email).value();
        final var refreshToken = tokenProvider.createRefresh(email).value();
        final var command = new LogoutCommand(TYPE + accessToken, TYPE + refreshToken);

        // WHEN
        final ThrowingCallable when = () -> authCommandService.logout(command);

        // THEN
        assertThatThrownBy(when).isInstanceOf(BadCredentialsException.class);
        then(expiredTokenCommandPort).should(never()).expire(any());
        then(refreshTokenCommandPort).should(never()).expire(any());
    }

    @Test
    void 유효하지_않은_리프레시_토큰_만료() {
        // GIVEN
        final var email = "email@email.com";
        final var expiredProvider = TokenProviderHelper.createExpired();
        final var accessToken = tokenProvider.createAccess(email).value();
        final var refreshToken = expiredProvider.createRefresh(email).value();
        final var command = new LogoutCommand(TYPE + accessToken, TYPE + refreshToken);

        // WHEN
        final ThrowingCallable when = () -> authCommandService.logout(command);

        // THEN
        assertThatThrownBy(when).isInstanceOf(BadCredentialsException.class);
        then(expiredTokenCommandPort).should(never()).expire(any());
        then(refreshTokenCommandPort).should(never()).expire(any());
    }

    @Test
    void 리프레시_토큰으로_엑세스_토큰_재발급() {
        // GIVEN
        final var email = "email@email.com";
        final var accessToken = tokenProvider.createAccess(email);
        final var refreshToken = tokenProvider.createRefresh(email);
        given(refreshTokenQueryPort.isExpired(refreshToken.value())).willReturn(Boolean.FALSE);
        final var command =
                new RefreshCommand(TYPE + accessToken.value(), TYPE + refreshToken.value());

        // WHEN
        authCommandService.refresh(command);

        // THEN
        then(expiredTokenCommandPort).should().expire(any());
    }

    @Test
    void 만료된_리프레시_토큰으로_엑세스_토큰_재발급() {
        // GIVEN
        final var email = "email@email.com";
        final var accessToken = tokenProvider.createAccess(email);
        final var refreshToken = tokenProvider.createRefresh(email);
        given(refreshTokenQueryPort.isExpired(refreshToken.value())).willReturn(Boolean.TRUE);
        final var command =
                new RefreshCommand(TYPE + accessToken.value(), TYPE + refreshToken.value());

        // WHEN
        final ThrowingCallable when = () -> authCommandService.refresh(command);

        // THEN
        assertThatThrownBy(when).isInstanceOf(BadCredentialsException.class);
    }
}
