package me.jaeyeop.blog.auth.application.service;

import me.jaeyeop.blog.auth.adapter.in.AuthRequest.Refresh;
import me.jaeyeop.blog.auth.application.port.in.AuthQueryUseCase;
import me.jaeyeop.blog.auth.application.port.out.RefreshTokenQueryPort;
import me.jaeyeop.blog.auth.domain.Token;
import me.jaeyeop.blog.config.token.TokenProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class AuthQueryService implements AuthQueryUseCase {

  private final RefreshTokenQueryPort refreshTokenQueryPort;

  private final TokenProvider tokenProvider;

  public AuthQueryService(final RefreshTokenQueryPort refreshTokenQueryPort,
      final TokenProvider tokenProvider) {
    this.refreshTokenQueryPort = refreshTokenQueryPort;
    this.tokenProvider = tokenProvider;
  }

  @Override
  public String refresh(final Refresh request) {
    final var refreshToken = authenticate(request);
    final var accessToken = tokenProvider.createAccess(refreshToken.email());

    return accessToken.value();
  }

  private Token authenticate(final Refresh request) {
    final var refreshToken = tokenProvider.authenticate(request.refreshToken());

    if (refreshTokenQueryPort.isExpired(refreshToken.value())) {
      throw new BadCredentialsException("Bad credentials");
    }

    return refreshToken;
  }

}
