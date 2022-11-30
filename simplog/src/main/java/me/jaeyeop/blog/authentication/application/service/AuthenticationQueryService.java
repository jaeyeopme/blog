package me.jaeyeop.blog.authentication.application.service;

import me.jaeyeop.blog.authentication.adapter.in.AuthenticationRequest.Refresh;
import me.jaeyeop.blog.authentication.application.port.in.AuthenticationQueryUseCase;
import me.jaeyeop.blog.authentication.application.port.out.RefreshTokenQueryPort;
import me.jaeyeop.blog.authentication.domain.Token;
import me.jaeyeop.blog.config.token.TokenProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class AuthenticationQueryService implements AuthenticationQueryUseCase {

  private final RefreshTokenQueryPort refreshTokenQueryPort;

  private final TokenProvider tokenProvider;

  public AuthenticationQueryService(final RefreshTokenQueryPort refreshTokenQueryPort,
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
