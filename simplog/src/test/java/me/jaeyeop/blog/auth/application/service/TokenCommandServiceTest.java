package me.jaeyeop.blog.auth.application.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import me.jaeyeop.blog.auth.adapter.out.ExpiredTokenRepository;
import me.jaeyeop.blog.auth.adapter.out.RefreshTokenRepository;
import me.jaeyeop.blog.auth.adapter.out.TokenPersistenceAdapter;
import me.jaeyeop.blog.auth.application.port.in.TokenProvideUseCase;
import me.jaeyeop.blog.auth.application.port.out.TokenCommandPort;
import me.jaeyeop.blog.config.security.JWTProvideServiceFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TokenCommandServiceTest {

  @Spy
  private TokenCommandPort tokenCommandPort = new TokenPersistenceAdapter(
      Mockito.mock(ExpiredTokenRepository.class), mock(RefreshTokenRepository.class));

  @Spy
  private TokenProvideUseCase jwtProvider = JWTProvideServiceFactory.create();

  @InjectMocks
  private TokenCommandService authCommandService;

  @Test
  void 만료_저장소에_만료되지_않은_토큰_저장() {
    final var accessToken = jwtProvider.createAccess("email@email.com");
    final var refreshToken = jwtProvider.createRefresh("email@email.com");

    authCommandService.logout(accessToken, refreshToken);

    then(tokenCommandPort).should().expire(any(), any());
  }

}