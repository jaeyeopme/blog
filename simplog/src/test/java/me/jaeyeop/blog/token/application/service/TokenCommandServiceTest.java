package me.jaeyeop.blog.token.application.service;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import me.jaeyeop.blog.config.token.TokenProvider;
import me.jaeyeop.blog.config.token.TokenProviderFactory;
import me.jaeyeop.blog.token.adapter.in.command.LogoutCommand;
import me.jaeyeop.blog.token.adapter.out.ExpiredTokenPersistenceAdapter;
import me.jaeyeop.blog.token.adapter.out.ExpiredTokenRepository;
import me.jaeyeop.blog.token.adapter.out.RefreshTokenPersistenceAdapter;
import me.jaeyeop.blog.token.adapter.out.RefreshTokenRepository;
import me.jaeyeop.blog.token.application.port.in.TokenCommandUseCase;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.authentication.BadCredentialsException;

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
    final var accessToken = tokenProvider.createAccess(email).getValue();
    final var refreshToken = tokenProvider.createRefresh(email).getValue();
    final var command = new LogoutCommand(accessToken, refreshToken);

    final ThrowingCallable when = () -> tokenCommandUseCase.logout(command);

    assertThatNoException().isThrownBy(when);
    then(expiredTokenRepository).should().save(any());
    then(refreshTokenRepository).should().delete(any());
  }

  @Test
  void 유효하지_않은_엑세스_토큰_만료() {
    final var email = "email@email.com";
    final var expiredProvider = TokenProviderFactory.createExpired();
    final var accessToken = expiredProvider.createAccess(email).getValue();
    final var refreshToken = tokenProvider.createRefresh(email).getValue();
    final var command = new LogoutCommand(accessToken, refreshToken);

    final ThrowingCallable when = () -> tokenCommandUseCase.logout(command);

    assertThatThrownBy(when).isInstanceOf(BadCredentialsException.class);
    then(expiredTokenRepository).should(never()).save(any());
    then(refreshTokenRepository).should(never()).delete(any());
  }

  @Test
  void 유효하지_않은_리프레시_토큰_만료() {
    final var email = "email@email.com";
    final var expiredProvider = TokenProviderFactory.createExpired();
    final var accessToken = expiredProvider.createAccess(email).getValue();
    final var refreshToken = tokenProvider.createRefresh(email).getValue();
    final var command = new LogoutCommand(accessToken, refreshToken);

    final ThrowingCallable when = () -> tokenCommandUseCase.logout(command);

    assertThatThrownBy(when).isInstanceOf(BadCredentialsException.class);
    then(expiredTokenRepository).should(never()).save(any());
    then(refreshTokenRepository).should(never()).delete(any());
  }

}