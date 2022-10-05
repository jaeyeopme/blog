package me.jaeyeop.blog.auth.adapter.in;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import me.jaeyeop.blog.auth.adapter.out.RefreshTokenRepository;
import me.jaeyeop.blog.auth.application.service.TokenCommandService;
import me.jaeyeop.blog.auth.application.service.TokenQueryService;
import me.jaeyeop.blog.config.token.TokenProvider;
import me.jaeyeop.blog.support.WebMvcTestSupport;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;

@Import({TokenQueryService.class, TokenCommandService.class})
class AuthWebAdaptorTest extends WebMvcTestSupport {

  @Autowired
  private TokenProvider tokenProvider;

  @Autowired
  private RefreshTokenRepository refreshTokenRepository;

  @Test
  void 토큰_만료_성공() throws Exception {
    final var accessToken = tokenProvider.createAccess("email@email.com").getValue();
    final var refreshToken = tokenProvider.createRefresh("email@email.com").getValue();

    final var given = get(AuthWebAdaptor.AUTH_API_URI + "/logout")
        .header(HttpHeaders.AUTHORIZATION, accessToken)
        .header(AuthWebAdaptor.REFRESH_AUTHORIZATION, refreshToken);

    final var when = mockMvc.perform(given);

    when.andExpectAll(status().isOk());
  }

  @Test
  void 토큰_재발급_성공() throws Exception {
    final var accessToken = tokenProvider.createAccess("email@email.com").getValue();
    final var refreshToken = tokenProvider.createRefresh("email@email.com").getValue();
    given(refreshTokenRepository.existsById(any())).willReturn(Boolean.TRUE);

    final var given = get(AuthWebAdaptor.AUTH_API_URI + "/refresh")
        .header(HttpHeaders.AUTHORIZATION, accessToken)
        .header(AuthWebAdaptor.REFRESH_AUTHORIZATION, refreshToken);

    final var when = mockMvc.perform(given);

    when.andExpectAll(status().isCreated());
  }


  @Test
  void 토큰_재발급_실패() throws Exception {
    final var accessToken = tokenProvider.createAccess("email@email.com").getValue();
    final var refreshToken = tokenProvider.createRefresh("email@email.com").getValue();
    given(refreshTokenRepository.existsById(any())).willReturn(Boolean.FALSE);

    final var given = get(AuthWebAdaptor.AUTH_API_URI + "/refresh")
        .header(HttpHeaders.AUTHORIZATION, accessToken)
        .header(AuthWebAdaptor.REFRESH_AUTHORIZATION, refreshToken);

    final var when = mockMvc.perform(given);

    when.andExpectAll(status().isUnauthorized());
  }

}