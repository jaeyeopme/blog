package me.jaeyeop.blog.token.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import me.jaeyeop.blog.config.token.TokenProvider;
import me.jaeyeop.blog.config.token.TokenProviderFactory;
import me.jaeyeop.blog.token.adapter.in.RefreshCommand;
import me.jaeyeop.blog.token.adapter.out.RefreshTokenPersistenceAdapter;
import me.jaeyeop.blog.token.adapter.out.RefreshTokenRepository;
import me.jaeyeop.blog.token.application.port.in.TokenQueryUseCase;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.authentication.BadCredentialsException;

class TokenQueryServiceTest {

  private RefreshTokenRepository refreshTokenRepository;

  private TokenProvider tokenProvider;

  private TokenQueryUseCase tokenQueryUseCase;

  @BeforeEach
  void setUp() {
    refreshTokenRepository = Mockito.mock(RefreshTokenRepository.class);
    tokenProvider = TokenProviderFactory.createDefault();
    tokenQueryUseCase = new TokenQueryService(
        new RefreshTokenPersistenceAdapter(refreshTokenRepository),
        tokenProvider
    );
  }

  @Test
  void 리프레시_토큰으로_엑세스_토큰_발급() {
    final var email = "email@email.com";
    final var refreshToken = tokenProvider.createRefresh(email);
    final var command = new RefreshCommand(refreshToken.getValue());
    final var expected = tokenProvider.createAccess(refreshToken.getEmail()).getValue();
    given(refreshTokenRepository.existsById(command.getRefreshToken())).willReturn(Boolean.TRUE);

    final var actual = tokenQueryUseCase.refresh(command);

    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void 리프레시_토큰_저장소에_없는_리프레시_토큰으로_엑세스_토큰_발급() {
    final var email = "email@email.com";
    final var refreshToken = tokenProvider.createRefresh(email);
    final var command = new RefreshCommand(refreshToken.getValue());
    given(refreshTokenRepository.existsById(command.getRefreshToken())).willReturn(Boolean.FALSE);

    final ThrowingCallable when = () -> tokenQueryUseCase.refresh(command);

    assertThatThrownBy(when).isInstanceOf(BadCredentialsException.class);
  }

}