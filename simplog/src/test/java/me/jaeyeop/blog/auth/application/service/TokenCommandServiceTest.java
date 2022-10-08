package me.jaeyeop.blog.auth.application.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import me.jaeyeop.blog.auth.adapter.in.LogoutCommand;
import me.jaeyeop.blog.auth.adapter.out.ExpiredTokenPersistenceAdapter;
import me.jaeyeop.blog.auth.adapter.out.ExpiredTokenRepository;
import me.jaeyeop.blog.auth.adapter.out.RefreshTokenPersistenceAdapter;
import me.jaeyeop.blog.auth.adapter.out.RefreshTokenRepository;
import me.jaeyeop.blog.auth.application.port.in.TokenCommandUseCase;
import me.jaeyeop.blog.config.token.TokenProvider;
import me.jaeyeop.blog.config.token.TokenProviderFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class TokenCommandServiceTest {

  private ExpiredTokenRepository expiredTokenRepository;

  private RefreshTokenRepository refreshTokenRepository;

  private TokenProvider tokenProvider;

  private TokenCommandUseCase tokenCommandUseCase;

  @BeforeEach
  void setUp() {
    expiredTokenRepository = Mockito.mock(ExpiredTokenRepository.class);
    refreshTokenRepository = Mockito.mock(RefreshTokenRepository.class);
    tokenProvider = TokenProviderFactory.createDefault();
    tokenCommandUseCase = new TokenCommandService(
        new ExpiredTokenPersistenceAdapter(expiredTokenRepository),
        new RefreshTokenPersistenceAdapter(refreshTokenRepository),
        tokenProvider
    );
  }

  @Test
  void 토큰_만료() {
    final var email = "email@email.com";
    final var accessToken = tokenProvider.createAccess(email);
    final var refreshToken = tokenProvider.createRefresh(email);
    final var command = new LogoutCommand(accessToken.getValue(), refreshToken.getValue());

    tokenCommandUseCase.logout(command);

    then(expiredTokenRepository).should().save(any());
    then(refreshTokenRepository).should().delete(any());
  }

}