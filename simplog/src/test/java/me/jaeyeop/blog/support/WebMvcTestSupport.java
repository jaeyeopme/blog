package me.jaeyeop.blog.support;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import me.jaeyeop.blog.config.ClockTestConfig;
import me.jaeyeop.blog.config.MockRepositoryTestConfig;
import me.jaeyeop.blog.config.SecurityTestConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

@Disabled
@Import({
    SecurityTestConfig.class,
    ClockTestConfig.class,
    MockRepositoryTestConfig.class,
})
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
