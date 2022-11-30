package me.jaeyeop.blog.authentication.adapter.out;

import me.jaeyeop.blog.authentication.application.port.out.AccessTokenCommandPort;
import me.jaeyeop.blog.authentication.application.port.out.AccessTokenQueryPort;
import me.jaeyeop.blog.authentication.domain.AccessToken;
import org.springframework.stereotype.Component;

@Component
public class AccessTokenPersistenceAdapter
    implements AccessTokenCommandPort, AccessTokenQueryPort {

  private final AccessTokenRepository accessTokenRepository;

  public AccessTokenPersistenceAdapter(final AccessTokenRepository accessTokenRepository) {
    this.accessTokenRepository = accessTokenRepository;
  }

  @Override
  public void expire(final AccessToken token) {
    accessTokenRepository.save(token);
  }

  @Override
  public boolean isExpired(final String token) {
    return accessTokenRepository.existsById(token);
  }

}
