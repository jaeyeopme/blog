package me.jaeyeop.blog.token.application.service;

import lombok.RequiredArgsConstructor;
import me.jaeyeop.blog.config.token.TokenProvider;
import me.jaeyeop.blog.token.adapter.in.RefreshCommand;
import me.jaeyeop.blog.token.application.port.in.TokenQueryUseCase;
import me.jaeyeop.blog.token.application.port.out.RefreshTokenQueryPort;
import me.jaeyeop.blog.token.domain.Token;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class TokenQueryService implements TokenQueryUseCase {

  private final RefreshTokenQueryPort refreshTokenQueryPort;

  private final TokenProvider tokenProvider;

  @Override
  public String refresh(final RefreshCommand command) {
    final var refreshToken = validate(command);
    final var accessToken = tokenProvider.createAccess(refreshToken.getEmail());

    return accessToken.getValue();
  }

  private Token validate(final RefreshCommand command) {
    final var refreshToken = tokenProvider.authenticate(command.getRefreshToken());

    if (refreshTokenQueryPort.isExpired(refreshToken.getValue())) {
      throw new BadCredentialsException("Bad credentials");
    }

    return refreshToken;
  }

}
