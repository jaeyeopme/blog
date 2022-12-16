package me.jaeyeop.blog.support;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import javax.persistence.EntityManager;
import me.jaeyeop.blog.config.security.authentication.UserPrincipal;
import me.jaeyeop.blog.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

/**
 * @author jaeyeopme Created on 12/01/2022.
 */
@Disabled
@Tag("integration")
@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = RANDOM_PORT)
public abstract class IntegrationTest extends ContainerTest {

  @Autowired
  protected WebApplicationContext context;

  @Autowired
  protected MockMvc mockMvc;

  @Autowired
  protected ObjectMapper objectMapper;

  @Autowired
  protected EntityManager entityManager;

  @BeforeEach
  public void setUp() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
        .alwaysDo(print())
        .addFilter(new CharacterEncodingFilter(StandardCharsets.UTF_8.name(), true))
        .build();
  }

  protected String toJson(final Object value) throws JsonProcessingException {
    return objectMapper.writeValueAsString(value);
  }

  protected User getPrincipal() {
    return ((UserPrincipal) SecurityContextHolder.getContext().getAuthentication()
        .getPrincipal()).user();
  }

  protected void clearPersistenceContext() {
    entityManager.flush();
    entityManager.clear();
  }

}
