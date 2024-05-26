package me.jaeyeop.blog.authentication.application.service;

import javax.transaction.Transactional;
import me.jaeyeop.blog.authentication.application.port.in.AuthenticationCommandUseCase;
import me.jaeyeop.blog.authentication.application.port.out.ExpiredTokenCommandPort;
import me.jaeyeop.blog.authentication.application.port.out.ExpiredTokenQueryPort;
import me.jaeyeop.blog.authentication.application.port.out.RefreshTokenCommandPort;
import me.jaeyeop.blog.authentication.application.port.out.RefreshTokenQueryPort;
import me.jaeyeop.blog.authentication.domain.ExpiredToken;
import me.jaeyeop.blog.authentication.domain.RefreshToken;
import me.jaeyeop.blog.authentication.domain.Token;
import me.jaeyeop.blog.commons.token.TokenProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

/**
 * @author jaeyeopme Created on 10/02/2022.
 */
@Transactional
@Service
public class AuthenticationCommandService implements AuthenticationCommandUseCase {

  private final ExpiredTokenCommandPort expiredTokenCommandPort;

  private final ExpiredTokenQueryPort expiredTokenQueryPort;

  private final RefreshTokenCommandPort refreshTokenCommandPort;

  private final RefreshTokenQueryPort refreshTokenQueryPort;

  private final TokenProvider tokenProvider;

  public AuthenticationCommandService(
      final ExpiredTokenQueryPort expiredTokenQueryPort,
      final ExpiredTokenCommandPort expiredTokenCommandPort,
      final RefreshTokenCommandPort refreshTokenCommandPort,
      final RefreshTokenQueryPort refreshTokenQueryPort,
      final TokenProvider tokenProvider
  ) {
    this.expiredTokenQueryPort = expiredTokenQueryPort;
    this.expiredTokenCommandPort = expiredTokenCommandPort;
    this.refreshTokenCommandPort = refreshTokenCommandPort;
    this.refreshTokenQueryPort = refreshTokenQueryPort;
    this.tokenProvider = tokenProvider;
  }

  @Override
  public void logout(final LogoutCommand command) {
    final var accessToken = tokenProvider.verify(command.accessToken());
    final var refreshToken = tokenProvider.verify(command.refreshToken());

    expireAccessToken(accessToken);
    expireRefreshToken(refreshToken);
  }

  @Override
  public String refresh(final RefreshCommand command) {
    final var verifiedAccessToken = tokenProvider.verify(command.accessToken());
    final var verifiedRefreshToken = tokenProvider.verify(command.refreshToken());

    if (refreshTokenQueryPort.isExpired(verifiedRefreshToken.value())) {
      throw new BadCredentialsException("Expired refresh token");
    }

    expiredTokenCommandPort.expire(ExpiredToken.from(verifiedAccessToken));

    return tokenProvider.createAccess(verifiedRefreshToken.email()).value();
  }

  private void expireAccessToken(final Token accessToken) {
    if (!expiredTokenQueryPort.isExpired(accessToken.value())) {
      expiredTokenCommandPort.expire(ExpiredToken.from(accessToken));
    }
  }

  private void expireRefreshToken(final Token refreshToken) {
    if (!refreshTokenQueryPort.isExpired(refreshToken.value())) {
      refreshTokenCommandPort.expire(RefreshToken.from(refreshToken));
    }
  }

}
