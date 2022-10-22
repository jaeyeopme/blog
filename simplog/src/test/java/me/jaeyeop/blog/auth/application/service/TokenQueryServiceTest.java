package me.jaeyeop.blog.auth.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import me.jaeyeop.blog.auth.adapter.in.AuthRequest.Refresh;
import me.jaeyeop.blog.auth.application.port.out.RefreshTokenQueryPort;
import me.jaeyeop.blog.auth.domain.Token;
import me.jaeyeop.blog.config.token.JWTProvider;
import me.jaeyeop.blog.config.token.TokenProvider;
import me.jaeyeop.blog.config.token.TokenProviderFactory;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;

@ExtendWith(MockitoExtension.class)
class TokenQueryServiceTest {

  @Mock(stubOnly = true)
  private RefreshTokenQueryPort refreshTokenQueryPort;

  @Spy
  private TokenProvider tokenProvider = TokenProviderFactory.createDefault();

  @InjectMocks
  private AuthQueryService authQueryService;


  @Test
  void 리프레시_토큰으로_엑세스_토큰_발급() {
    final var email = "email@email.com";
    final var refreshToken = tokenProvider.createRefresh(email);
    final var expected = tokenProvider.createAccess(email);
    given(refreshTokenQueryPort.isExpired(refreshToken.value())).willReturn(
        Boolean.FALSE);

    final var actual = authQueryService.refresh(getRefresh(refreshToken));

    assertThat(actual).isEqualTo(expected.value());
  }

  @Test
  void 만료된_리프레시_토큰으로_엑세스_토큰_발급() {
    final var email = "email@email.com";
    final var refreshToken = tokenProvider.createRefresh(email);
    given(refreshTokenQueryPort.isExpired(refreshToken.value())).willReturn(Boolean.TRUE);

    final ThrowingCallable when = () -> authQueryService.refresh(getRefresh(refreshToken));

    assertThatThrownBy(when).isInstanceOf(BadCredentialsException.class);
  }

  private Refresh getRefresh(final Token refreshToken) {
    return new Refresh(JWTProvider.TYPE + refreshToken.value());
  }

}