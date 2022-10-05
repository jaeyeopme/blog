package me.jaeyeop.blog.auth.application.service;

import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import me.jaeyeop.blog.auth.adapter.in.LogoutCommand;
import me.jaeyeop.blog.auth.adapter.out.ExpiredToken;
import me.jaeyeop.blog.auth.adapter.out.RefreshToken;
import me.jaeyeop.blog.auth.application.port.in.TokenCommandUseCase;
import me.jaeyeop.blog.auth.application.port.out.ExpiredTokenCommandPort;
import me.jaeyeop.blog.auth.application.port.out.RefreshTokenCommandPort;
import me.jaeyeop.blog.auth.domain.Token;
import me.jaeyeop.blog.config.token.TokenProvider;
import org.springframework.stereotype.Service;

@Transactional
@Service
@RequiredArgsConstructor
public class TokenCommandService implements TokenCommandUseCase {

  private final ExpiredTokenCommandPort expiredTokenCommandPort;

  private final RefreshTokenCommandPort refreshTokenCommandPort;

  private final TokenProvider tokenProvider;

  @Override
  public void logout(final LogoutCommand command) {
    final var accessToken = tokenProvider.authenticate(command.getAccessToken());
    final var refreshToken = tokenProvider.authenticate(command.getRefreshToken());

    expiredTokenCommandPort.expire(getExpiredToken(accessToken));
    refreshTokenCommandPort.expire(getRefreshToken(refreshToken));
  }

  private ExpiredToken getExpiredToken(final Token accessToken) {
    return new ExpiredToken(accessToken.getValue(), accessToken.getRemaining());
  }

  private RefreshToken getRefreshToken(final Token refreshToken) {
    return new RefreshToken(refreshToken.getValue(), refreshToken.getExpiration());
  }

}
