package me.jaeyeop.blog.auth.adapter.out;

import lombok.RequiredArgsConstructor;
import me.jaeyeop.blog.auth.application.port.out.ExpiredTokenCommandPort;
import me.jaeyeop.blog.auth.application.port.out.ExpiredTokenQueryPort;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ExpiredTokenPersistenceAdapter
    implements ExpiredTokenCommandPort, ExpiredTokenQueryPort {

  private final ExpiredTokenRepository expiredTokenRepository;

  @Override
  public void expire(final ExpiredToken token) {
    expiredTokenRepository.save(token);
  }

  @Override
  public boolean isExpired(final String token) {
    return expiredTokenRepository.existsById(token);
  }

}
