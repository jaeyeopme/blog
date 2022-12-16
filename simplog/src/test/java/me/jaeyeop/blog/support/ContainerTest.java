package me.jaeyeop.blog.support;

import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * @author jaeyeopme Created on 12/13/2022.
 */
@SuppressWarnings({"rawtypes", "resource"})
@DirtiesContext
@Testcontainers
public abstract class ContainerTest {

  private static final String MARIADB_IMAGE = "mariadb:10.7.3";
  private static final String REDIS_IMAGE = "redis:7.0.4-alpine";
  private static final int REDIS_PORT = 6379;

  @Container
  private static final MariaDBContainer MARIADB_CONTAINER;

  @Container
  private static final GenericContainer REDIS_CONTAINER;

  static {
    MARIADB_CONTAINER = new MariaDBContainer<>(MARIADB_IMAGE)
        .withDatabaseName("blog")
        .withExposedPorts(3306)
        .withUsername("test")
        .withPassword("test")
        .withReuse(true);

    REDIS_CONTAINER = new GenericContainer<>(REDIS_IMAGE)
        .withExposedPorts(REDIS_PORT)
        .withReuse(true);
  }

  @DynamicPropertySource
  private static void setProperty(final DynamicPropertyRegistry registry) {
    registry.add("spring.redis.host", REDIS_CONTAINER::getHost);
    registry.add("spring.redis.port", () -> REDIS_CONTAINER.getMappedPort(REDIS_PORT));
    registry.add("spring.datasource.url", MARIADB_CONTAINER::getJdbcUrl);
    registry.add("spring.datasource.driver-class-name", MARIADB_CONTAINER::getDriverClassName);
    registry.add("spring.datasource.hikari.username", MARIADB_CONTAINER::getUsername);
    registry.add("spring.datasource.hikari.password", MARIADB_CONTAINER::getPassword);
  }

}
