package me.jaeyeop.blog.config;


import me.jaeyeop.blog.config.token.TokenProvider;
import me.jaeyeop.blog.support.helper.TokenProviderHelper;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class SecurityTestConfig {

  @Bean
  public TokenProvider tokenProvider() {
    return TokenProviderHelper.create();
  }

}
