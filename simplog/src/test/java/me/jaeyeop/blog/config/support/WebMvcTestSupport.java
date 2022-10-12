package me.jaeyeop.blog.config.support;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import me.jaeyeop.blog.config.clock.ClockTestConfig;
import me.jaeyeop.blog.config.security.SecurityTestConfig;
import me.jaeyeop.blog.post.adapter.out.PostCommandRepository;
import me.jaeyeop.blog.post.adapter.out.PostPersistenceAdapter;
import me.jaeyeop.blog.post.adapter.out.PostQueryRepository;
import me.jaeyeop.blog.token.adapter.out.ExpiredTokenPersistenceAdapter;
import me.jaeyeop.blog.token.adapter.out.ExpiredTokenRepository;
import me.jaeyeop.blog.token.adapter.out.RefreshTokenPersistenceAdapter;
import me.jaeyeop.blog.token.adapter.out.RefreshTokenRepository;
import me.jaeyeop.blog.user.adapter.out.UserPersistenceAdapter;
import me.jaeyeop.blog.user.adapter.out.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

@Disabled
@Import({
    SecurityTestConfig.class,
    ClockTestConfig.class,
    ExpiredTokenPersistenceAdapter.class,
    RefreshTokenPersistenceAdapter.class,
    UserPersistenceAdapter.class,
    PostPersistenceAdapter.class
})
@MockBeans({
    @MockBean(ExpiredTokenRepository.class),
    @MockBean(RefreshTokenRepository.class),
    @MockBean(UserRepository.class),
    @MockBean(PostCommandRepository.class),
    @MockBean(PostQueryRepository.class)})
public abstract class WebMvcTestSupport {

  protected MockMvc mockMvc;

  @Autowired
  private WebApplicationContext context;

  @Autowired
  private ObjectMapper objectMapper;

  @BeforeEach
  public void setUp() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
        .alwaysDo(print())
        .addFilter(new CharacterEncodingFilter(StandardCharsets.UTF_8.name(), true))
        .build();
  }

  protected String toJson(final Object obj) throws JsonProcessingException {
    return objectMapper.writeValueAsString(obj);
  }

}
