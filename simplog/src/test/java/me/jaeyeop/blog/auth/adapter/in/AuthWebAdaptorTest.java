package me.jaeyeop.blog.auth.adapter.in;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import me.jaeyeop.blog.auth.adapter.out.RefreshTokenRepository;
import me.jaeyeop.blog.auth.application.service.TokenCommandService;
import me.jaeyeop.blog.auth.application.service.TokenQueryService;
import me.jaeyeop.blog.config.support.WebMvcTestSupport;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;

@Import({TokenQueryService.class, TokenCommandService.class})
@WebMvcTest(AuthWebAdaptor.class)
class AuthWebAdaptorTest extends WebMvcTestSupport {

  @Autowired
  private RefreshTokenRepository refreshTokenRepository;

  @Test
  void 로그아웃() throws Exception {
    final var given = getWithAuthHeader(AuthWebAdaptor.AUTH_API_URI + "/logout");

    final var when = mockMvc.perform(given);

    when.andExpectAll(status().isOk());
  }

  @Test
  void 리프레시_토큰으로_엑세스_토큰_재발급() throws Exception {
    given(refreshTokenRepository.existsById(any())).willReturn(Boolean.TRUE);
    final var given = getWithAuthHeader(AuthWebAdaptor.AUTH_API_URI + "/refresh");

    final var when = mockMvc.perform(given);

    when.andExpectAll(status().isCreated());
  }


  @Test
  void 만료된_리프레시_토큰으로_엑세스_토큰_재발급() throws Exception {
    given(refreshTokenRepository.existsById(any())).willReturn(Boolean.FALSE);
    final var given = getWithAuthHeader(AuthWebAdaptor.AUTH_API_URI + "/refresh");

    final var when = mockMvc.perform(given);

    when.andExpectAll(status().isUnauthorized());
  }

}