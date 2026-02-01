package me.jaeyeop.blog.authentication.application.service;

import java.util.Optional;

import jakarta.transaction.Transactional;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.stereotype.Service;

import me.jaeyeop.blog.authentication.application.port.in.AuthenticationCommandUseCase;
import me.jaeyeop.blog.authentication.application.port.out.ExpiredTokenCommandPort;
import me.jaeyeop.blog.authentication.application.port.out.ExpiredTokenQueryPort;
import me.jaeyeop.blog.authentication.application.port.out.RefreshTokenCommandPort;
import me.jaeyeop.blog.authentication.application.port.out.RefreshTokenQueryPort;
import me.jaeyeop.blog.authentication.domain.ExpiredToken;
import me.jaeyeop.blog.authentication.domain.RefreshToken;
import me.jaeyeop.blog.authentication.domain.Token;
import me.jaeyeop.blog.authentication.domain.TokenClaims;
import me.jaeyeop.blog.commons.authentication.OAuth2Response;
import me.jaeyeop.blog.commons.error.exception.UserNotFoundException;
import me.jaeyeop.blog.commons.token.TokenProvider;
import me.jaeyeop.blog.user.application.port.out.UserQueryPort;
import me.jaeyeop.blog.user.domain.User;

@Transactional
@Service
public class AuthenticationCommandService implements AuthenticationCommandUseCase {
    private final ExpiredTokenCommandPort expiredTokenCommandPort;

    private final ExpiredTokenQueryPort expiredTokenQueryPort;

    private final RefreshTokenCommandPort refreshTokenCommandPort;

    private final RefreshTokenQueryPort refreshTokenQueryPort;

    private final TokenProvider tokenProvider;

    private final UserQueryPort userQueryPort;

    public AuthenticationCommandService(
            final ExpiredTokenQueryPort expiredTokenQueryPort,
            final ExpiredTokenCommandPort expiredTokenCommandPort,
            final RefreshTokenCommandPort refreshTokenCommandPort,
            final RefreshTokenQueryPort refreshTokenQueryPort,
            final TokenProvider tokenProvider,
            final UserQueryPort userQueryPort) {
        this.expiredTokenQueryPort = expiredTokenQueryPort;
        this.expiredTokenCommandPort = expiredTokenCommandPort;
        this.refreshTokenCommandPort = refreshTokenCommandPort;
        this.refreshTokenQueryPort = refreshTokenQueryPort;
        this.tokenProvider = tokenProvider;
        this.userQueryPort = userQueryPort;
    }

    @Override
    public void logout(final LogoutCommand command) {
        resolveTokenIgnoringExpiration(command.accessToken())
                .ifPresent(this::invalidateAccessToken);
        resolveTokenIgnoringExpiration(command.refreshToken())
                .ifPresent(this::invalidateRefreshToken);
    }

    @Override
    public OAuth2Response refresh(final RefreshCommand command) {
        final var verifiedRefreshToken = tokenProvider.verify(command.refreshToken());

        validateRefreshToken(verifiedRefreshToken);

        final var user = getUser(verifiedRefreshToken);
        final var tokenClaims = new TokenClaims(user.id(), user.roles());

        final var newAccessToken = tokenProvider.createAccess(tokenClaims);
        final var newRefreshToken = tokenProvider.createRefresh(tokenClaims);

        rotateRefreshToken(verifiedRefreshToken, newRefreshToken);

        return new OAuth2Response(newAccessToken.value(), newRefreshToken.value());
    }

    private Optional<Token> resolveTokenIgnoringExpiration(final String token) {
        try {
            return Optional.of(tokenProvider.verify(token));
        } catch (final CredentialsExpiredException ignored) {
            return Optional.empty();
        }
    }

    private void invalidateAccessToken(final Token token) {
        if (!expiredTokenQueryPort.isInvalidated(token.value())) {
            expiredTokenCommandPort.invalidate(ExpiredToken.from(token));
        }
    }

    private void invalidateRefreshToken(final Token token) {
        if (!refreshTokenQueryPort.isInvalidated(token.value())) {
            refreshTokenCommandPort.invalidate(RefreshToken.from(token));
        }
    }

    private void rotateRefreshToken(final Token oldToken, final Token newToken) {
        invalidateRefreshToken(oldToken);
        refreshTokenCommandPort.activate(RefreshToken.from(newToken));
    }

    private void validateRefreshToken(final Token refreshToken) {
        if (refreshTokenQueryPort.isInvalidated(refreshToken.value())) {
            throw new BadCredentialsException("Invalid refresh token");
        }
    }

    private User getUser(final Token token) {
        final var userId = token.tokenClaims().id();
        return userQueryPort.findById(userId).orElseThrow(UserNotFoundException::new);
    }
}
