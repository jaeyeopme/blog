package me.jaeyeop.blog.token.adapter.out;

import me.jaeyeop.blog.token.application.port.out.RefreshTokenCommandPort;
import me.jaeyeop.blog.token.application.port.out.RefreshTokenQueryPort;
import me.jaeyeop.blog.token.domain.RefreshToken;
import org.springframework.stereotype.Component;

@Component
public class RefreshTokenPersistenceAdapter
    implements RefreshTokenQueryPort, RefreshTokenCommandPort {

  private final RefreshTokenRepository refreshTokenRepository;

  public RefreshTokenPersistenceAdapter(final RefreshTokenRepository refreshTokenRepository) {
    this.refreshTokenRepository = refreshTokenRepository;
  }

  @Override
  public boolean isExpired(final String token) {
    return !refreshTokenRepository.existsById(token);
  }

  @Override
  public void expire(final RefreshToken token) {
    refreshTokenRepository.delete(token);
  }

  @Override
  public void activateRefresh(final RefreshToken token) {
    refreshTokenRepository.save(token);
  }

}
