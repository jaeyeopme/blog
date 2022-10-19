package me.jaeyeop.blog.token.adapter.out;

import me.jaeyeop.blog.token.application.port.out.ExpiredTokenCommandPort;
import me.jaeyeop.blog.token.application.port.out.ExpiredTokenQueryPort;
import me.jaeyeop.blog.token.domain.ExpiredToken;
import org.springframework.stereotype.Component;

@Component
public class ExpiredTokenPersistenceAdapter
    implements ExpiredTokenCommandPort, ExpiredTokenQueryPort {

  private final ExpiredTokenRepository expiredTokenRepository;

  public ExpiredTokenPersistenceAdapter(final ExpiredTokenRepository expiredTokenRepository) {
    this.expiredTokenRepository = expiredTokenRepository;
  }

  @Override
  public void expire(final ExpiredToken token) {
    expiredTokenRepository.save(token);
  }

  @Override
  public boolean isExpired(final String token) {
    return expiredTokenRepository.existsById(token);
  }

}
