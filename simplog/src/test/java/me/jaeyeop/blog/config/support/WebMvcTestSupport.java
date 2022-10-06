package me.jaeyeop.blog.config.support;

import static me.jaeyeop.blog.user.domain.UserFactory.DEFAULT_USER_EMAIL;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import me.jaeyeop.blog.auth.adapter.in.AuthWebAdaptor;
import me.jaeyeop.blog.auth.adapter.out.ExpiredTokenPersistenceAdapter;
import me.jaeyeop.blog.auth.adapter.out.ExpiredTokenRepository;
import me.jaeyeop.blog.auth.adapter.out.RefreshTokenPersistenceAdapter;
import me.jaeyeop.blog.auth.adapter.out.RefreshTokenRepository;
import me.jaeyeop.blog.auth.domain.Token;
import me.jaeyeop.blog.config.clock.ClockTestConfig;
import me.jaeyeop.blog.config.security.SecurityTestConfig;
import me.jaeyeop.blog.config.token.TokenProvider;
import me.jaeyeop.blog.user.adapter.out.UserPersistenceAdapter;
import me.jaeyeop.blog.user.adapter.out.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

@Disabled
@Import({
    SecurityTestConfig.class,
    ClockTestConfig.class,
    UserPersistenceAdapter.class,
    ExpiredTokenPersistenceAdapter.class,
    RefreshTokenPersistenceAdapter.class
})
public abstract class WebMvcTestSupport {

  protected MockMvc mockMvc;

  @MockBean
  protected UserRepository userRepository;

  @MockBean
  protected ExpiredTokenRepository expiredTokenRepository;

  @MockBean
  protected RefreshTokenRepository refreshTokenRepository;

  private Token accessToken;

  private Token refreshToken;

  @Autowired
  private WebApplicationContext context;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private TokenProvider tokenProvider;

  @BeforeEach
  public void setUp() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
        .alwaysDo(print())
        .addFilter(new CharacterEncodingFilter(StandardCharsets.UTF_8.name(), true))
        .build();
    this.accessToken = tokenProvider.createAccess(DEFAULT_USER_EMAIL);
    this.refreshToken = tokenProvider.createRefresh(DEFAULT_USER_EMAIL);
  }

  protected String toJson(final Object obj) throws JsonProcessingException {
    return objectMapper.writeValueAsString(obj);
  }

  protected MockHttpServletRequestBuilder getWithAuthHeader(
      final String urlTemplate,
      final Object... variables) {
    return MockMvcRequestBuilders.get(urlTemplate, variables)
        .header(HttpHeaders.AUTHORIZATION, accessToken.getValue())
        .header(AuthWebAdaptor.REFRESH_AUTHORIZATION, refreshToken.getValue());
  }

}
