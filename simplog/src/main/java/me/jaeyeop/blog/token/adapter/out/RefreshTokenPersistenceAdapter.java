package me.jaeyeop.blog.token.adapter.out;

import lombok.RequiredArgsConstructor;
import me.jaeyeop.blog.token.application.port.out.RefreshTokenCommandPort;
import me.jaeyeop.blog.token.application.port.out.RefreshTokenQueryPort;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RefreshTokenPersistenceAdapter
    implements RefreshTokenQueryPort, RefreshTokenCommandPort {

  private final RefreshTokenRepository refreshTokenRepository;

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
