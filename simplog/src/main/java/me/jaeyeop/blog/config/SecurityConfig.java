package me.jaeyeop.blog.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import me.jaeyeop.blog.config.security.OAuth2AuthenticationFilter;
import me.jaeyeop.blog.config.security.OAuth2SuccessHandler;
import me.jaeyeop.blog.config.security.OAuth2UserServiceDelegator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Configuration
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class SecurityConfig {

  public static final String OAUTH2_API_URI = "/api/v1/oauth2";

  private final OAuth2AuthenticationFilter oAuth2AuthenticationFilter;

  private final OAuth2SuccessHandler oAuth2SuccessHandler;

  private final OAuth2UserServiceDelegator oAuth2UserServiceDelegator;

  private final HandlerExceptionResolver handlerExceptionResolver;

  @Bean
  public SecurityFilterChain securityFilterChain(final HttpSecurity httpSecurity) throws Exception {
    httpSecurity
        .csrf().disable()
        .cors().disable()
        .formLogin().disable()
        .httpBasic().disable()
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

    httpSecurity
        .addFilterAt(oAuth2AuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
        .oauth2Login()
        .successHandler(oAuth2SuccessHandler)
        .userInfoEndpoint()
        .userService(oAuth2UserServiceDelegator)
        .and()
        .authorizationEndpoint()
        .baseUri(OAUTH2_API_URI);

    httpSecurity
        .exceptionHandling()
        .accessDeniedHandler(getAccessDeniedHandler())
        .authenticationEntryPoint(getAuthenticationEntryPoint());

    httpSecurity
        .authorizeRequests(getAuthorizeRequests());

    return httpSecurity.build();
  }

  private AuthenticationEntryPoint getAuthenticationEntryPoint() {
    return this::resolveException;
  }

  private AccessDeniedHandler getAccessDeniedHandler() {
    return this::resolveException;
  }

  private void resolveException(final HttpServletRequest request,
      final HttpServletResponse response,
      final RuntimeException exception) {
    handlerExceptionResolver.resolveException(request, response, null, exception);
  }

  private Customizer<ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry> getAuthorizeRequests() {
    return urlRegistry -> urlRegistry
        .mvcMatchers(HttpMethod.GET, OAUTH2_API_URI)
        .anonymous()
        .anyRequest()
        .authenticated();
  }

}
