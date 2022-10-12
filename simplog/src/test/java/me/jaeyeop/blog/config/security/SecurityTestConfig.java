package me.jaeyeop.blog.config.security;


import me.jaeyeop.blog.config.token.TokenProvider;
import me.jaeyeop.blog.config.token.TokenProviderFactory;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class SecurityTestConfig {

  @Bean
  public TokenProvider tokenProvider() {
    return TokenProviderFactory.createDefault();
  }

}
