package me.jaeyeop.blog.support;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.jaeyeop.blog.auth.adapter.out.ExpiredTokenRepository;
import me.jaeyeop.blog.auth.adapter.out.RefreshTokenRepository;
import me.jaeyeop.blog.auth.adapter.out.TokenPersistenceAdapter;
import me.jaeyeop.blog.config.security.ClockTestConfig;
import me.jaeyeop.blog.config.security.SecurityTestConfig;
import me.jaeyeop.blog.user.adapter.out.UserPersistenceAdapter;
import me.jaeyeop.blog.user.adapter.out.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@Disabled
@Import({
    SecurityTestConfig.class,
    ClockTestConfig.class,
    UserPersistenceAdapter.class,
    TokenPersistenceAdapter.class,
})
@WebMvcTest
public abstract class WebMvcTestSupport {

  protected MockMvc mockMvc;

  @MockBean
  protected UserRepository userRepository;

  @MockBean
  protected ExpiredTokenRepository expiredTokenRepository;

  @MockBean
  protected RefreshTokenRepository refreshTokenRepository;

  @Autowired
  private WebApplicationContext context;

  @Autowired
  private ObjectMapper objectMapper;

  @BeforeEach
  public void setUp() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
        .alwaysDo(print())
        .build();
  }

  protected String toJson(final Object obj) throws JsonProcessingException {
    return objectMapper.writeValueAsString(obj);
  }

}
