package me.jaeyeop.blog.authentication.adapter.in;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import me.jaeyeop.blog.authentication.adapter.out.RefreshTokenRepository;
import me.jaeyeop.blog.authentication.application.service.AuthenticationCommandService;
import me.jaeyeop.blog.authentication.application.service.AuthenticationQueryService;
import me.jaeyeop.blog.config.token.JWTProvider;
import me.jaeyeop.blog.config.token.TokenProvider;
import me.jaeyeop.blog.support.WebMvcTestSupport;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;

@Import({AuthenticationQueryService.class, AuthenticationCommandService.class})
@WebMvcTest(AuthenticationWebAdaptor.class)
class AuthenticationWebAdaptorTest extends WebMvcTestSupport {

  @Autowired
  private RefreshTokenRepository refreshTokenRepository;

  @Autowired
  private TokenProvider tokenProvider;

  @Test
  void 토큰_만료() throws Exception {
    final var accessToken = getAccessToken();
    final var refreshToken = getRefreshToken();

    final var when = mockMvc.perform(
        delete(AuthenticationWebAdaptor.AUTHENTICATION_API_URI + "/expire")
            .header(HttpHeaders.AUTHORIZATION, accessToken)
            .header(AuthenticationWebAdaptor.REFRESH_AUTHORIZATION, refreshToken));

    when.andExpectAll(status().isNoContent());
  }

  @Test
  void 리프레시_토큰으로_엑세스_토큰_발급() throws Exception {
    final var accessToken = getAccessToken();
    final var refreshToken = getRefreshToken();
    given(refreshTokenRepository.existsById(any())).willReturn(Boolean.TRUE);

    final var when = mockMvc.perform(
        post(AuthenticationWebAdaptor.AUTHENTICATION_API_URI + "/refresh")
            .header(HttpHeaders.AUTHORIZATION, accessToken)
            .header(AuthenticationWebAdaptor.REFRESH_AUTHORIZATION, refreshToken));

    when.andExpectAll(status().isCreated());
  }

  @Test
  void 리프레시_토큰_저장소에_없는_리프레시_토큰으로_엑세스_토큰_발급() throws Exception {
    final var accessToken = getAccessToken();
    final var refreshToken = getRefreshToken();
    given(refreshTokenRepository.existsById(any())).willReturn(Boolean.FALSE);

    final var when = mockMvc.perform(
        post(AuthenticationWebAdaptor.AUTHENTICATION_API_URI + "/refresh")
            .header(HttpHeaders.AUTHORIZATION, accessToken)
            .header(AuthenticationWebAdaptor.REFRESH_AUTHORIZATION, refreshToken));

    when.andExpectAll(status().isUnauthorized());
  }

  private String getRefreshToken() {
    return JWTProvider.TYPE + tokenProvider.createRefresh("email@email.com").value();
  }

  private String getAccessToken() {
    return JWTProvider.TYPE + tokenProvider.createAccess("email@email.com").value();
  }

}