package me.jaeyeop.blog.token.adapter.in;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import me.jaeyeop.blog.config.token.TokenProvider;
import me.jaeyeop.blog.support.WebMvcTestSupport;
import me.jaeyeop.blog.token.adapter.out.RefreshTokenRepository;
import me.jaeyeop.blog.token.application.service.TokenCommandService;
import me.jaeyeop.blog.token.application.service.TokenQueryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;

@Import({TokenQueryService.class, TokenCommandService.class})
@WebMvcTest(TokenWebAdaptor.class)
class TokenWebAdaptorTest extends WebMvcTestSupport {

  @Autowired
  private RefreshTokenRepository refreshTokenRepository;

  @Autowired
  private TokenProvider tokenProvider;

  @Test
  void 로그아웃() throws Exception {
    final var email = "email@email.com";
    final var accessToken = tokenProvider.createAccess(email).getValue();
    final var refreshToken = tokenProvider.createRefresh(email).getValue();

    final var when = mockMvc.perform(
        get(TokenWebAdaptor.AUTH_API_URI + "/logout")
            .header(HttpHeaders.AUTHORIZATION, accessToken)
            .header(TokenWebAdaptor.REFRESH_AUTHORIZATION, refreshToken));

    when.andExpectAll(status().isOk());
  }

  @Test
  void 리프레시_토큰으로_엑세스_토큰_발급() throws Exception {
    final var email = "email@email.com";
    final var accessToken = tokenProvider.createAccess(email).getValue();
    final var refreshToken = tokenProvider.createRefresh(email).getValue();
    given(refreshTokenRepository.existsById(any())).willReturn(Boolean.TRUE);

    final var when = mockMvc.perform(
        get(TokenWebAdaptor.AUTH_API_URI + "/refresh")
            .header(HttpHeaders.AUTHORIZATION, accessToken)
            .header(TokenWebAdaptor.REFRESH_AUTHORIZATION, refreshToken));

    when.andExpectAll(status().isCreated());
  }

  @Test
  void 리프레시_토큰_저장소에_없는_리프레시_토큰으로_엑세스_토큰_발급() throws Exception {
    final var email = "email@email.com";
    final var accessToken = tokenProvider.createAccess(email).getValue();
    final var refreshToken = tokenProvider.createRefresh(email).getValue();
    given(refreshTokenRepository.existsById(any())).willReturn(Boolean.FALSE);

    final var when = mockMvc.perform(
        get(TokenWebAdaptor.AUTH_API_URI + "/refresh")
            .header(HttpHeaders.AUTHORIZATION, accessToken)
            .header(TokenWebAdaptor.REFRESH_AUTHORIZATION, refreshToken));

    when.andExpectAll(status().isUnauthorized());
  }

}