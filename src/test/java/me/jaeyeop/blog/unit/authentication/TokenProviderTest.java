package me.jaeyeop.blog.unit.authentication;

import java.util.Set;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;

import me.jaeyeop.blog.authentication.domain.TokenClaims;
import me.jaeyeop.blog.commons.token.TokenProvider;
import me.jaeyeop.blog.support.factory.TokenFactory;
import me.jaeyeop.blog.user.domain.Role;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TokenProviderTest {
    private TokenProvider tokenProvider;

    @BeforeEach
    void setUp() {
        tokenProvider = TokenFactory.create();
    }

    @Test
    void 엑세스_토큰_발급() {
        final var id = 55L;
        final var claims = new TokenClaims(id, Set.of(Role.USER));
        final var accessToken = tokenProvider.createAccess(claims);

        assertThat(accessToken.tokenClaims().id()).isEqualTo(id);
    }

    @Test
    void 리프레시_토큰_발급() {
        final var id = 55L;
        final var claims = new TokenClaims(id, Set.of(Role.USER));

        final var refreshToken = tokenProvider.createRefresh(claims);

        assertThat(refreshToken.tokenClaims().id()).isEqualTo(id);
    }

    @Test
    void 다른_키를_가진_토큰_검증() {
        final var differentKeyProvider = TokenFactory.createDifferentKey();
        final var claims = new TokenClaims(1L, Set.of(Role.USER));
        final var accessToken = differentKeyProvider.createAccess(claims);

        final ThrowingCallable when = () -> tokenProvider.verify(accessToken.value());

        assertThatThrownBy(when).isInstanceOf(BadCredentialsException.class);
    }

    @Test
    void 만료된_토큰_검증() {
        final var expiredProvider = TokenFactory.createExpired();
        final var claims = new TokenClaims(1L, Set.of(Role.USER));
        final var accessToken = expiredProvider.createAccess(claims);

        final ThrowingCallable when = () -> tokenProvider.verify(accessToken.value());

        assertThatThrownBy(when).isInstanceOf(CredentialsExpiredException.class);
    }
}
