package me.jaeyeop.blog.auth.application.service;

import java.time.Clock;
import java.time.Instant;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import me.jaeyeop.blog.auth.application.port.in.AuthCommandUseCase;
import me.jaeyeop.blog.auth.application.port.out.AuthCommandPort;
import me.jaeyeop.blog.auth.domain.ExpiredToken;
import me.jaeyeop.blog.auth.domain.JWTProvider;
import org.springframework.stereotype.Service;

@Transactional
@Service
@RequiredArgsConstructor
public class AuthCommandService implements AuthCommandUseCase {

  private final AuthCommandPort authCommandPort;

  private final JWTProvider jwtProvider;

  private final Clock clock;

  @Override
  public void expireToken(
      final String accessToken,
      final String refreshToken) {
    final var instant = clock.instant();

    saveToken(accessToken, instant);
    saveToken(refreshToken, instant);
  }

  private void saveToken(final String token, final Instant instant) {
    final var remaining = jwtProvider.getRemaining(token, instant);

    if (isRemaining(remaining)) {
      authCommandPort.save(new ExpiredToken(token, remaining));
    }
  }

  private boolean isRemaining(final long remaining) {
    return remaining != 0;
  }

}
