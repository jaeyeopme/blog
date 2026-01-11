package me.jaeyeop.blog.unit.authentication;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import me.jaeyeop.blog.commons.token.TokenProvider;
import me.jaeyeop.blog.support.factory.TokenFactory;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.BadCredentialsException;

class TokenProviderTest {
    private TokenProvider tokenProvider;

    @BeforeEach
    void setUp() {
        tokenProvider = TokenFactory.create();
    }

    @Test
    void 엑세스_토큰_발급() {
        final var email = "email@email.com";

        final var accessToken = tokenProvider.createAccess(email);

        assertThat(accessToken.email()).isEqualTo(email);
    }

    @Test
    void 리프레시_토큰_발급() {
        final var email = "email@email.com";

        final var refreshToken = tokenProvider.createRefresh(email);

        assertThat(refreshToken.email()).isEqualTo(email);
    }

    @Test
    void 다른_키를_가진_토큰_검증() {
        final var differentKeyProvider = TokenFactory.createDifferentKey();
        final var accessToken = differentKeyProvider.createAccess("email@email.com");

        final ThrowingCallable when = () -> tokenProvider.verify(accessToken.value());

        assertThatThrownBy(when).isInstanceOf(BadCredentialsException.class);
    }

    @Test
    void 만료된_토큰_검증() {
        final var expiredProvider = TokenFactory.createExpired();
        final var accessToken = expiredProvider.createAccess("email@email.com");

        final ThrowingCallable when = () -> tokenProvider.verify(accessToken.value());

        assertThatThrownBy(when).isInstanceOf(BadCredentialsException.class);
    }
}
