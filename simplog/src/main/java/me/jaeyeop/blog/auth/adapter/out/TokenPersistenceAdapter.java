package me.jaeyeop.blog.auth.adapter.out;

import lombok.RequiredArgsConstructor;
import me.jaeyeop.blog.auth.application.port.out.TokenCommandPort;
import me.jaeyeop.blog.auth.application.port.out.TokenQueryPort;
import me.jaeyeop.blog.auth.domain.Token;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TokenPersistenceAdapter implements TokenCommandPort, TokenQueryPort {

  private final ExpiredTokenRepository expiredTokenRepository;

  private final RefreshTokenRepository refreshTokenRepository;

  @Override
  public void expire(final Token access, final Token refresh) {
    expiredTokenRepository.save(ExpiredToken.from(access));
    refreshTokenRepository.delete(RefreshToken.from(refresh));
  }

  @Override
  public void activate(final Token refresh) {
    refreshTokenRepository.save(RefreshToken.from(refresh));
  }

  @Override
  public boolean isExpired(final String value) {
    return expiredTokenRepository.existsById(value);
  }

}
