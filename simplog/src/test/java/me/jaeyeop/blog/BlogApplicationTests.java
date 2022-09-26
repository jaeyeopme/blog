package me.jaeyeop.blog;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BlogApplicationTests extends AbstractContainerBaseTest {

  @Test
  void contextLoads() {
    assertThat(MARIA_DB_CONTAINER.isRunning()).isTrue();
  }

}
