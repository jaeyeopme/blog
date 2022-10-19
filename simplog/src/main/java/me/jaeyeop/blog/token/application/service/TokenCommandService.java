package me.jaeyeop.blog.token.application.service;

import javax.transaction.Transactional;
import me.jaeyeop.blog.config.token.TokenProvider;
import me.jaeyeop.blog.token.adapter.in.command.LogoutCommand;
import me.jaeyeop.blog.token.application.port.in.TokenCommandUseCase;
import me.jaeyeop.blog.token.application.port.out.ExpiredTokenCommandPort;
import me.jaeyeop.blog.token.application.port.out.RefreshTokenCommandPort;
import me.jaeyeop.blog.token.domain.ExpiredToken;
import me.jaeyeop.blog.token.domain.RefreshToken;
import me.jaeyeop.blog.token.domain.Token;
import org.springframework.stereotype.Service;

@Transactional
@Service
public class TokenCommandService implements TokenCommandUseCase {

  private final ExpiredTokenCommandPort expiredTokenCommandPort;

  private final RefreshTokenCommandPort refreshTokenCommandPort;

  private final TokenProvider tokenProvider;

  public TokenCommandService(final ExpiredTokenCommandPort expiredTokenCommandPort,
      final RefreshTokenCommandPort refreshTokenCommandPort,
      final TokenProvider tokenProvider) {
    this.expiredTokenCommandPort = expiredTokenCommandPort;
    this.refreshTokenCommandPort = refreshTokenCommandPort;
    this.tokenProvider = tokenProvider;
  }

  @Override
  public void logout(final LogoutCommand command) {
    final Token accessToken = tokenProvider.authenticate(command.getAccessToken());
    final Token refreshToken = tokenProvider.authenticate(command.getRefreshToken());

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
