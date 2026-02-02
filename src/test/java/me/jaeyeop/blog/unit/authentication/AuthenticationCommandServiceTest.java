package me.jaeyeop.blog.unit.authentication;

import java.util.List;
import java.util.Optional;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import me.jaeyeop.blog.authentication.application.port.in.AuthenticationCommandUseCase.LogoutCommand;
import me.jaeyeop.blog.authentication.application.port.in.AuthenticationCommandUseCase.RefreshCommand;
import me.jaeyeop.blog.authentication.application.port.out.ExpiredTokenCommandPort;
import me.jaeyeop.blog.authentication.application.port.out.ExpiredTokenQueryPort;
import me.jaeyeop.blog.authentication.application.port.out.RefreshTokenCommandPort;
import me.jaeyeop.blog.authentication.application.port.out.RefreshTokenQueryPort;
import me.jaeyeop.blog.authentication.application.service.AuthenticationCommandService;
import me.jaeyeop.blog.authentication.domain.TokenClaims;
import me.jaeyeop.blog.commons.token.TokenProvider;
import me.jaeyeop.blog.support.UnitTest;
import me.jaeyeop.blog.support.factory.TokenFactory;
import me.jaeyeop.blog.support.factory.UserFactory;
import me.jaeyeop.blog.user.application.port.out.UserQueryPort;
import me.jaeyeop.blog.user.domain.Role;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

class AuthenticationCommandServiceTest extends UnitTest {
    @InjectMocks
    private AuthenticationCommandService authCommandService;

    @Mock
    private ExpiredTokenCommandPort expiredTokenCommandPort;

    @Mock
    private RefreshTokenCommandPort refreshTokenCommandPort;

    @Mock(stubOnly = true)
    private ExpiredTokenQueryPort expiredTokenQueryPort;

    @Mock(stubOnly = true)
    private RefreshTokenQueryPort refreshTokenQueryPort;

    @Mock(stubOnly = true)
    private UserQueryPort userQueryPort;

    @Spy
    private TokenProvider tokenProvider = TokenFactory.create();

    @Test
    @DisplayName("Logout success")
    void logout_success() {
        final var tokenClaims =
                new TokenClaims(55L, List.of(new SimpleGrantedAuthority(Role.USER.name())));
        final var accessToken = tokenProvider.createAccess(tokenClaims).value();
        final var refreshToken = tokenProvider.createRefresh(tokenClaims).value();
        given(expiredTokenQueryPort.isInvalidated(accessToken)).willReturn(Boolean.FALSE);
        given(refreshTokenQueryPort.isInvalidated(refreshToken)).willReturn(Boolean.FALSE);
        final var command = new LogoutCommand(accessToken, refreshToken);

        authCommandService.logout(command);

        then(expiredTokenCommandPort).should().invalidate(any());
        then(refreshTokenCommandPort).should().invalidate(any());
    }

    @Test
    @DisplayName("Logout with invalidated access token")
    void logout_with_invalidated_access_token() {
        final var tokenClaims =
                new TokenClaims(55L, List.of(new SimpleGrantedAuthority(Role.USER.name())));
        final var accessToken = tokenProvider.createAccess(tokenClaims).value();
        final var refreshToken = tokenProvider.createRefresh(tokenClaims).value();
        given(expiredTokenQueryPort.isInvalidated(accessToken)).willReturn(Boolean.TRUE);
        given(refreshTokenQueryPort.isInvalidated(refreshToken)).willReturn(Boolean.FALSE);
        final var command = new LogoutCommand(accessToken, refreshToken);

        authCommandService.logout(command);

        then(expiredTokenCommandPort).should(never()).invalidate(any());
        then(refreshTokenCommandPort).should().invalidate(any());
    }

    @Test
    @DisplayName("Logout with invalidated refresh token")
    void logout_with_invalidated_refresh_token() {
        final var tokenClaims =
                new TokenClaims(55L, List.of(new SimpleGrantedAuthority(Role.USER.name())));
        final var accessToken = tokenProvider.createAccess(tokenClaims).value();
        final var refreshToken = tokenProvider.createRefresh(tokenClaims).value();
        given(expiredTokenQueryPort.isInvalidated(accessToken)).willReturn(Boolean.FALSE);
        given(refreshTokenQueryPort.isInvalidated(refreshToken)).willReturn(Boolean.TRUE);
        final var command = new LogoutCommand(accessToken, refreshToken);

        authCommandService.logout(command);

        then(expiredTokenCommandPort).should().invalidate(any());
        then(refreshTokenCommandPort).should(never()).invalidate(any());
    }

    @Test
    @DisplayName("Logout with invalid access token")
    void logout_with_invalid_access_token() {
        final var tokenClaims =
                new TokenClaims(55L, List.of(new SimpleGrantedAuthority(Role.USER.name())));
        final var expiredProvider = TokenFactory.createExpired();
        final var accessToken = expiredProvider.createAccess(tokenClaims).value();
        final var refreshToken = tokenProvider.createRefresh(tokenClaims).value();
        given(refreshTokenQueryPort.isInvalidated(refreshToken)).willReturn(Boolean.FALSE);
        final var command = new LogoutCommand(accessToken, refreshToken);

        authCommandService.logout(command);

        then(expiredTokenCommandPort).should(never()).invalidate(any());
        then(refreshTokenCommandPort).should().invalidate(any());
    }

    @Test
    @DisplayName("Logout with invalid refresh token")
    void logout_with_invalid_refresh_token() {
        final var tokenClaims =
                new TokenClaims(55L, List.of(new SimpleGrantedAuthority(Role.USER.name())));
        final var expiredProvider = TokenFactory.createExpired();
        final var accessToken = tokenProvider.createAccess(tokenClaims).value();
        final var refreshToken = expiredProvider.createRefresh(tokenClaims).value();
        given(expiredTokenQueryPort.isInvalidated(accessToken)).willReturn(Boolean.FALSE);
        final var command = new LogoutCommand(accessToken, refreshToken);

        authCommandService.logout(command);

        then(expiredTokenCommandPort).should().invalidate(any());
        then(refreshTokenCommandPort).should(never()).invalidate(any());
    }

    @Test
    @DisplayName("Refresh access token")
    void refresh_access_token() {
        final var user = UserFactory.create(55L);
        final var tokenClaims = new TokenClaims(user.id(), user.roles());
        final var refreshToken = tokenProvider.createRefresh(tokenClaims);
        given(refreshTokenQueryPort.isInvalidated(refreshToken.value())).willReturn(Boolean.FALSE);
        given(userQueryPort.findById(user.id())).willReturn(Optional.of(user));
        assertThat(refreshToken.value()).isNotBlank();

        final var command = new RefreshCommand(refreshToken.value());
        authCommandService.refresh(command);

        then(refreshTokenCommandPort).should().activate(any());
    }

    @Test
    @DisplayName("Refresh access token fail invalidated refresh token")
    void refresh_access_token_fail_invalidated_refresh_token() {
        final var tokenClaims =
                new TokenClaims(55L, List.of(new SimpleGrantedAuthority(Role.USER.name())));
        final var refreshToken = tokenProvider.createRefresh(tokenClaims);
        given(refreshTokenQueryPort.isInvalidated(refreshToken.value())).willReturn(Boolean.TRUE);

        final var command = new RefreshCommand(refreshToken.value());
        final ThrowingCallable when = () -> authCommandService.refresh(command);

        assertThatThrownBy(when).isInstanceOf(BadCredentialsException.class);
    }
}
