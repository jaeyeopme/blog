package me.jaeyeop.blog.auth.application.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import me.jaeyeop.blog.auth.application.port.out.AuthCommandPort;
import me.jaeyeop.blog.auth.domain.JWTProvider;
import me.jaeyeop.blog.config.security.JWTProviderFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AuthCommandServiceTest {

  @Mock
  private AuthCommandPort authCommandPort;

  @Spy
  private JWTProvider jwtProvider = JWTProviderFactory.create();

  @Spy
  private Clock clock = Clock.fixed(Instant.now(), ZoneId.systemDefault());

  @InjectMocks
  private AuthCommandService authCommandService;

  @Test
  void 만료_저장소에_만료되지_않은_토큰_저장() {
    final var accessToken = jwtProvider.issueAccessToken("email@email.com");
    final var refreshToken = jwtProvider.issueRefreshToken("email@email.com");

    authCommandService.expireToken(accessToken, refreshToken);

    then(authCommandPort).should(times(2)).save(any());
  }

  @Test
  void 만료_저장소에_만료된_토큰_저장() {
    final var expiredProvider = JWTProviderFactory.createExpiredProvider();
    final var accessToken = expiredProvider.issueAccessToken("email@email.com");
    final var refreshToken = expiredProvider.issueRefreshToken("email@email.com");

    authCommandService.expireToken(accessToken, refreshToken);

    then(authCommandPort).should(never()).save(any());
  }

}