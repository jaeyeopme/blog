package me.jaeyeop.blog.config;

import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

  @SneakyThrows
  @Bean
  public SecurityFilterChain securityFilterChain(final HttpSecurity httpSecurity) {
    httpSecurity
        .csrf().disable()
        .cors().disable()
        .formLogin().disable()
        .httpBasic().disable()
        .authorizeRequests(getAuthorizeRequests());

    return httpSecurity.build();
  }

  private Customizer<ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry> getAuthorizeRequests() {
    return auth -> auth.anyRequest().permitAll();
  }

}
