package me.jaeyeop.blog.support;

import com.redis.testcontainers.RedisContainer;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.utility.DockerImageName;

public abstract class ContainerTest {
    @ServiceConnection
    public static final MySQLContainer<?> MYSQL_CONTAINER =
            new MySQLContainer<>(DockerImageName.parse("mysql").withTag("8.4.7"))
                    .withDatabaseName("blog")
                    .withExposedPorts(3306)
                    .withUsername("test")
                    .withPassword("test");

    @ServiceConnection
    public static final RedisContainer REDIS_CONTAINER =
            new RedisContainer(DockerImageName.parse("redis").withTag("8.4.0-alpine"))
                    .withExposedPorts(RedisContainer.REDIS_PORT);

    static {
        MYSQL_CONTAINER.start();
        REDIS_CONTAINER.start();
    }
}
