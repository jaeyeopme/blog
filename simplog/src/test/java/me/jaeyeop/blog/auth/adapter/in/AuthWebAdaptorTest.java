package me.jaeyeop.blog.auth.adapter.in;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import me.jaeyeop.blog.auth.application.port.in.TokenProvideUseCase;
import me.jaeyeop.blog.auth.application.service.TokenCommandService;
import me.jaeyeop.blog.config.security.JWTProvideServiceFactory;
import me.jaeyeop.blog.support.WebMvcTestSupport;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;

@Import(TokenCommandService.class)
class AuthWebAdaptorTest extends WebMvcTestSupport {

  @Autowired
  private TokenProvideUseCase tokenProvideUseCase;

  @Test
  void 토큰_만료_성공() throws Exception {
    final var access = tokenProvideUseCase.createAccess("email@email.com").getValue();
    final var refresh = tokenProvideUseCase.createRefresh("email@email.com").getValue();
    final var given = get(AuthWebAdaptor.AUTH_API_URI + "/logout")
        .header(HttpHeaders.AUTHORIZATION, access)
        .header(AuthWebAdaptor.REFRESH_AUTHORIZATION, refresh);

    final var when = mockMvc.perform(given);

    when.andExpectAll(status().isOk());
  }

  @Test
  void 타입이_없는_토큰_만료_실패() throws Exception {
    final var access = tokenProvideUseCase.createAccess("email@email.com").getValue();
    final var refresh = tokenProvideUseCase.createRefresh("email@email.com").getValue();
    final var given = get(AuthWebAdaptor.AUTH_API_URI + "/logout")
        .header(HttpHeaders.AUTHORIZATION, access.substring(7))
        .header(AuthWebAdaptor.REFRESH_AUTHORIZATION, refresh.substring(7));

    final var when = mockMvc.perform(given);

    when.andExpectAll(status().isUnauthorized());
  }

  @Test
  void 유효하지_않은_토큰_만료_실패() throws Exception {
    final var wrongKey = JWTProvideServiceFactory.createWrongKey();
    final var access = wrongKey.createAccess("email@email.com").getValue();
    final var refresh = wrongKey.createRefresh("email@email.com").getValue();
    final var given = get(AuthWebAdaptor.AUTH_API_URI + "/logout")
        .header(HttpHeaders.AUTHORIZATION, access)
        .header(AuthWebAdaptor.REFRESH_AUTHORIZATION, refresh);

    final var when = mockMvc.perform(given);

    when.andExpectAll(status().isUnauthorized());
  }

  @Test
  void 만료된_토큰_만료_실패() throws Exception {
    final var expired = JWTProvideServiceFactory.createExpired();
    final var access = expired.createAccess("email@email.com").getValue();
    final var refresh = expired.createRefresh("email@email.com").getValue();
    final var given = get(AuthWebAdaptor.AUTH_API_URI + "/logout")
        .header(HttpHeaders.AUTHORIZATION, access)
        .header(AuthWebAdaptor.REFRESH_AUTHORIZATION, refresh);

    final var when = mockMvc.perform(given);

    when.andExpectAll(status().isUnauthorized());
  }

}