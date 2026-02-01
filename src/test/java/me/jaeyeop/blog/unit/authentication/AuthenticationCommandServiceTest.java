package me.jaeyeop.blog.unit.authentication;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

import java.util.List;
import java.util.Optional;
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
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

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
    void 로그아웃_성공() {
        // GIVEN
        final var tokenClaims =
                new TokenClaims(55L, List.of(new SimpleGrantedAuthority(Role.USER.name())));
        final var accessToken = tokenProvider.createAccess(tokenClaims).value();
        final var refreshToken = tokenProvider.createRefresh(tokenClaims).value();
        given(expiredTokenQueryPort.isInvalidated(accessToken)).willReturn(Boolean.FALSE);
        given(refreshTokenQueryPort.isInvalidated(refreshToken)).willReturn(Boolean.FALSE);
        final var command = new LogoutCommand(accessToken, refreshToken);

        // WHEN
        authCommandService.logout(command);

        // THEN
        then(expiredTokenCommandPort).should().invalidate(any());
        then(refreshTokenCommandPort).should().invalidate(any());
    }

    @Test
    void 이미_무효화된_엑세스_토큰_로그아웃() {
        // GIVEN
        final var tokenClaims =
                new TokenClaims(55L, List.of(new SimpleGrantedAuthority(Role.USER.name())));
        final var accessToken = tokenProvider.createAccess(tokenClaims).value();
        final var refreshToken = tokenProvider.createRefresh(tokenClaims).value();
        given(expiredTokenQueryPort.isInvalidated(accessToken)).willReturn(Boolean.TRUE);
        given(refreshTokenQueryPort.isInvalidated(refreshToken)).willReturn(Boolean.FALSE);
        final var command = new LogoutCommand(accessToken, refreshToken);

        // WHEN
        authCommandService.logout(command);

        // THEN
        then(expiredTokenCommandPort).should(never()).invalidate(any());
        then(refreshTokenCommandPort).should().invalidate(any());
    }

    @Test
    void 이미_무효화된_리프레시_토큰_로그아웃() {
        // GIVEN
        final var tokenClaims =
                new TokenClaims(55L, List.of(new SimpleGrantedAuthority(Role.USER.name())));
        final var accessToken = tokenProvider.createAccess(tokenClaims).value();
        final var refreshToken = tokenProvider.createRefresh(tokenClaims).value();
        given(expiredTokenQueryPort.isInvalidated(accessToken)).willReturn(Boolean.FALSE);
        given(refreshTokenQueryPort.isInvalidated(refreshToken)).willReturn(Boolean.TRUE);
        final var command = new LogoutCommand(accessToken, refreshToken);

        // WHEN
        authCommandService.logout(command);

        // THEN
        then(expiredTokenCommandPort).should().invalidate(any());
        then(refreshTokenCommandPort).should(never()).invalidate(any());
    }

    @Test
    void 유효하지_않은_엑세스_토큰_로그아웃() {
        // GIVEN
        final var tokenClaims =
                new TokenClaims(55L, List.of(new SimpleGrantedAuthority(Role.USER.name())));
        final var expiredProvider = TokenFactory.createExpired();
        final var accessToken = expiredProvider.createAccess(tokenClaims).value();
        final var refreshToken = tokenProvider.createRefresh(tokenClaims).value();
        given(refreshTokenQueryPort.isInvalidated(refreshToken)).willReturn(Boolean.FALSE);
        final var command = new LogoutCommand(accessToken, refreshToken);

        // WHEN
        authCommandService.logout(command);

        // THEN: Logout should succeed even if the token is invalid
        then(expiredTokenCommandPort).should(never()).invalidate(any());
        then(refreshTokenCommandPort).should().invalidate(any());
    }

    @Test
    void 유효하지_않은_리프레시_토큰_로그아웃() {
        // GIVEN
        final var tokenClaims =
                new TokenClaims(55L, List.of(new SimpleGrantedAuthority(Role.USER.name())));
        final var expiredProvider = TokenFactory.createExpired();
        final var accessToken = tokenProvider.createAccess(tokenClaims).value();
        final var refreshToken = expiredProvider.createRefresh(tokenClaims).value();
        given(expiredTokenQueryPort.isInvalidated(accessToken)).willReturn(Boolean.FALSE);
        final var command = new LogoutCommand(accessToken, refreshToken);

        // WHEN
        authCommandService.logout(command);

        // THEN: Logout should succeed even if the token is invalid
        then(expiredTokenCommandPort).should().invalidate(any());
        then(refreshTokenCommandPort).should(never()).invalidate(any());
    }

    @Test
    void 리프레시_토큰으로_엑세스_토큰_재발급() {
        // GIVEN
        final var user = UserFactory.create(55L);
        final var tokenClaims = new TokenClaims(user.id(), user.roles());
        final var refreshToken = tokenProvider.createRefresh(tokenClaims);
        given(refreshTokenQueryPort.isInvalidated(refreshToken.value())).willReturn(Boolean.FALSE);
        given(userQueryPort.findById(user.id())).willReturn(Optional.of(user));
        assertThat(refreshToken.value()).isNotBlank();

        // WHEN
        final var command = new RefreshCommand(refreshToken.value());
        authCommandService.refresh(command);

        // THEN
        then(refreshTokenCommandPort).should().activate(any());
    }

    @Test
    void 무효화된_리프레시_토큰으로_엑세스_토큰_재발급_실패() {
        // GIVEN
        final var tokenClaims =
                new TokenClaims(55L, List.of(new SimpleGrantedAuthority(Role.USER.name())));
        final var refreshToken = tokenProvider.createRefresh(tokenClaims);
        given(refreshTokenQueryPort.isInvalidated(refreshToken.value())).willReturn(Boolean.TRUE);

        // WHEN
        final var command = new RefreshCommand(refreshToken.value());
        final ThrowingCallable when = () -> authCommandService.refresh(command);

        // THEN
        assertThatThrownBy(when).isInstanceOf(BadCredentialsException.class);
    }
}
