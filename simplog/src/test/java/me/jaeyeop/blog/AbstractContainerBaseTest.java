package me.jaeyeop.blog;

import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public abstract class AbstractContainerBaseTest {

  @Container
  static final MariaDBContainer<?> MARIA_DB_CONTAINER;

  static {
    MARIA_DB_CONTAINER = new MariaDBContainer<>("mariadb:10.3")
        .withDatabaseName("blog")
        .withExposedPorts(3306)
        .withUsername("test")
        .withPassword("test");
  }

}
