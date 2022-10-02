package me.jaeyeop.blog.auth.adapter.out;

import lombok.RequiredArgsConstructor;
import me.jaeyeop.blog.auth.application.port.out.AuthCommandPort;
import me.jaeyeop.blog.auth.domain.ExpiredToken;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ExpiredTokenPersistenceAdapter implements AuthCommandPort {

  private final ExpiredTokenRepository expiredTokenRepository;

  @Override
  public void save(final ExpiredToken expiredToken) {
    expiredTokenRepository.save(expiredToken);
  }

}
