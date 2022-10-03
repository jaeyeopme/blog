package me.jaeyeop.blog.auth.adapter.in;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import me.jaeyeop.blog.auth.application.service.AuthCommandService;
import me.jaeyeop.blog.auth.domain.JWTProvider;
import me.jaeyeop.blog.support.WebMvcTestSupport;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;

@Import(AuthCommandService.class)
class AuthWebAdaptorTest extends WebMvcTestSupport {

  @Autowired
  protected JWTProvider jwtProvider;

  @Test
  void 토큰_만료_성공() throws Exception {
    final var given = get(AuthWebAdaptor.AUTH_API_URI + "/logout")
        .header(HttpHeaders.AUTHORIZATION,
            jwtProvider.issueAccessToken("email@email.com"))
        .header(AuthWebAdaptor.REFRESH_AUTHORIZATION,
            jwtProvider.issueRefreshToken("email@email.com"));

    final var when = mockMvc.perform(given);

    when.andExpectAll(status().isOk());
  }

}