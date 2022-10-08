package me.jaeyeop.blog.auth.adapter.in;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import me.jaeyeop.blog.auth.adapter.out.RefreshTokenRepository;
import me.jaeyeop.blog.auth.application.service.TokenCommandService;
import me.jaeyeop.blog.auth.application.service.TokenQueryService;
import me.jaeyeop.blog.config.support.WebMvcTestSupport;
import me.jaeyeop.blog.config.token.TokenProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;

@Import({TokenQueryService.class, TokenCommandService.class})
@WebMvcTest(AuthWebAdaptor.class)
class AuthWebAdaptorTest extends WebMvcTestSupport {

  @Autowired
  private RefreshTokenRepository refreshTokenRepository;

  @Autowired
  private TokenProvider tokenProvider;

  @Test
  void 로그아웃() throws Exception {
    final var email = "email@email.com";
    final var given = get(AuthWebAdaptor.AUTH_API_URI + "/logout")
        .header(HttpHeaders.AUTHORIZATION,
            tokenProvider.createAccess(email).getValue())
        .header(AuthWebAdaptor.REFRESH_AUTHORIZATION,
            tokenProvider.createRefresh(email).getValue());

    final var when = mockMvc.perform(given);

    when.andExpectAll(status().isOk());
  }

  @Test
  void 리프레시_토큰으로_엑세스_토큰_발급() throws Exception {
    final var email = "email@email.com";
    final var refreshToken = tokenProvider.createRefresh(email);
    given(refreshTokenRepository.existsById(any())).willReturn(Boolean.TRUE);
    final var given = get(AuthWebAdaptor.AUTH_API_URI + "/refresh")
        .header(HttpHeaders.AUTHORIZATION,
            tokenProvider.createAccess(email).getValue())
        .header(AuthWebAdaptor.REFRESH_AUTHORIZATION,
            refreshToken.getValue());

    final var when = mockMvc.perform(given);

    when.andExpectAll(status().isCreated());
  }

  @Test
  void 리프레시_토큰_저장소에_없는_리프레시_토큰으로_엑세스_토큰_발급() throws Exception {
    final var email = "email@email.com";
    final var refreshToken = tokenProvider.createRefresh(email);
    given(refreshTokenRepository.existsById(any())).willReturn(Boolean.FALSE);
    final var given = get(AuthWebAdaptor.AUTH_API_URI + "/refresh")
        .header(HttpHeaders.AUTHORIZATION,
            tokenProvider.createAccess(email).getValue())
        .header(AuthWebAdaptor.REFRESH_AUTHORIZATION,
            refreshToken.getValue());

    final var when = mockMvc.perform(given);

    when.andExpectAll(status().isUnauthorized());
  }

}