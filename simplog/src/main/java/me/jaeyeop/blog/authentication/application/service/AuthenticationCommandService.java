package me.jaeyeop.blog.authentication.application.service;

import javax.transaction.Transactional;
import me.jaeyeop.blog.authentication.adapter.in.AuthenticationRequest.Expire;
import me.jaeyeop.blog.authentication.application.port.in.AuthenticationCommandUseCase;
import me.jaeyeop.blog.authentication.application.port.out.AccessTokenCommandPort;
import me.jaeyeop.blog.authentication.application.port.out.AccessTokenQueryPort;
import me.jaeyeop.blog.authentication.application.port.out.RefreshTokenCommandPort;
import me.jaeyeop.blog.authentication.application.port.out.RefreshTokenQueryPort;
import me.jaeyeop.blog.authentication.domain.AccessToken;
import me.jaeyeop.blog.authentication.domain.RefreshToken;
import me.jaeyeop.blog.authentication.domain.Token;
import me.jaeyeop.blog.config.token.TokenProvider;
import org.springframework.stereotype.Service;

@Transactional
@Service
public class AuthenticationCommandService implements AuthenticationCommandUseCase {

  private final AccessTokenCommandPort accessTokenCommandPort;

  private final AccessTokenQueryPort accessTokenQueryPort;

  private final RefreshTokenCommandPort refreshTokenCommandPort;

  private final RefreshTokenQueryPort refreshTokenQueryPort;

  private final TokenProvider tokenProvider;

  public AuthenticationCommandService(
      final AccessTokenQueryPort accessTokenQueryPort,
      final AccessTokenCommandPort accessTokenCommandPort,
      final RefreshTokenCommandPort refreshTokenCommandPort,
      final RefreshTokenQueryPort refreshTokenQueryPort,
      final TokenProvider tokenProvider) {
    this.accessTokenQueryPort = accessTokenQueryPort;
    this.accessTokenCommandPort = accessTokenCommandPort;
    this.refreshTokenCommandPort = refreshTokenCommandPort;
    this.refreshTokenQueryPort = refreshTokenQueryPort;
    this.tokenProvider = tokenProvider;
  }

  @Override
  public void expire(final Expire request) {
    final var accessToken = tokenProvider.authenticate(request.accessToken());
    final var refreshToken = tokenProvider.authenticate(request.refreshToken());

    expireAccessToken(accessToken);
    expireRefreshToken(refreshToken);
  }

  private void expireAccessToken(final Token accessToken) {
    if (!accessTokenQueryPort.isExpired(accessToken.value())) {
      accessTokenCommandPort.expire(
          new AccessToken(accessToken.value(), accessToken.remaining()));
    }
  }

  private void expireRefreshToken(final Token refreshToken) {
    if (!refreshTokenQueryPort.isExpired(refreshToken.value())) {
      refreshTokenCommandPort.expire(
          new RefreshToken(refreshToken.value(), refreshToken.expiration()));
    }
  }

}
