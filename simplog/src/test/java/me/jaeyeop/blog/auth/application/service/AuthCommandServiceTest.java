package me.jaeyeop.blog.auth.application.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import me.jaeyeop.blog.auth.application.port.out.AuthCommandPort;
import me.jaeyeop.blog.config.security.JWTProvider;
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
  void 만료_저장소에_토큰_저장() {
    final var access_token = jwtProvider.issueAccessToken("email@email.com");
    final var refresh_token = jwtProvider.issueRefreshToken("email@email.com");

    authCommandService.expireToken(access_token, refresh_token);

    then(authCommandPort).should(times(2)).save(any());
  }


}