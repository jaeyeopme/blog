package me.jaeyeop.blog.auth.adapter.out;

import me.jaeyeop.blog.auth.application.port.out.RefreshTokenCommandPort;
import me.jaeyeop.blog.auth.application.port.out.RefreshTokenQueryPort;
import me.jaeyeop.blog.auth.domain.RefreshToken;
import org.springframework.stereotype.Component;

@Component
public class RefreshTokenPersistenceAdapter
    implements RefreshTokenCommandPort, RefreshTokenQueryPort {

  private final RefreshTokenRepository refreshTokenRepository;

  public RefreshTokenPersistenceAdapter(final RefreshTokenRepository refreshTokenRepository) {
    this.refreshTokenRepository = refreshTokenRepository;
  }

  @Override
  public void activate(final RefreshToken token) {
    refreshTokenRepository.save(token);
  }

  @Override
  public void expire(final RefreshToken token) {
    refreshTokenRepository.delete(token);
  }

  @Override
  public boolean isExpired(final String token) {
    return !refreshTokenRepository.existsById(token);
  }

}
