package me.jaeyeop.blog.auth.application.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import me.jaeyeop.blog.auth.adapter.in.LogoutCommand;
import me.jaeyeop.blog.auth.adapter.out.ExpiredTokenPersistenceAdapter;
import me.jaeyeop.blog.auth.adapter.out.ExpiredTokenRepository;
import me.jaeyeop.blog.auth.adapter.out.RefreshTokenPersistenceAdapter;
import me.jaeyeop.blog.auth.adapter.out.RefreshTokenRepository;
import me.jaeyeop.blog.auth.application.port.out.ExpiredTokenCommandPort;
import me.jaeyeop.blog.auth.application.port.out.RefreshTokenCommandPort;
import me.jaeyeop.blog.config.security.JWTProviderFactory;
import me.jaeyeop.blog.config.token.TokenProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TokenCommandServiceTest {

  @Spy
  private ExpiredTokenCommandPort expiredTokenCommandPort = new ExpiredTokenPersistenceAdapter(
      Mockito.mock(ExpiredTokenRepository.class));

  @Spy
  private RefreshTokenCommandPort refreshTokenCommandPort = new RefreshTokenPersistenceAdapter(
      Mockito.mock(RefreshTokenRepository.class));

  @Spy
  private TokenProvider jwtProvider = JWTProviderFactory.create();

  @InjectMocks
  private TokenCommandService authCommandService;

  @Test
  void 만료_저장소에_만료되지_않은_토큰_저장() {
    final var accessToken = jwtProvider.createAccess("email@email.com");
    final var refreshToken = jwtProvider.createRefresh("email@email.com");
    final var command = new LogoutCommand(accessToken.getValue(), refreshToken.getValue());

    authCommandService.logout(command);

    then(expiredTokenCommandPort).should().expire(any());
    then(refreshTokenCommandPort).should().expire(any());
  }

  @Test
  void 만료_저장소에_만료된_토큰_저장() {
    final var accessToken = jwtProvider.createAccess("email@email.com");
    final var refreshToken = jwtProvider.createRefresh("email@email.com");
    final var command = new LogoutCommand(accessToken.getValue(), refreshToken.getValue());

    authCommandService.logout(command);

    then(expiredTokenCommandPort).should().expire(any());
    then(refreshTokenCommandPort).should().expire(any());
  }

}