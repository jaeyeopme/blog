package me.jaeyeop.blog.config.security;

import static me.jaeyeop.blog.token.adapter.in.TokenWebAdaptor.AUTH_API_URI;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import me.jaeyeop.blog.post.adapter.in.PostWebAdapter;
import me.jaeyeop.blog.user.adapter.in.UserWebAdapter;
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
public class SecurityConfig {

  private final OAuth2AuthenticationFilter oAuth2AuthenticationFilter;

  private final OAuth2SuccessHandler oAuth2SuccessHandler;

  private final OAuth2UserServiceDelegator oAuth2UserServiceDelegator;

  private final HandlerExceptionResolver handlerExceptionResolver;

  public SecurityConfig(final OAuth2AuthenticationFilter oAuth2AuthenticationFilter,
      final OAuth2SuccessHandler oAuth2SuccessHandler,
      final OAuth2UserServiceDelegator oAuth2UserServiceDelegator,
      final HandlerExceptionResolver handlerExceptionResolver) {
    this.oAuth2AuthenticationFilter = oAuth2AuthenticationFilter;
    this.oAuth2SuccessHandler = oAuth2SuccessHandler;
    this.oAuth2UserServiceDelegator = oAuth2UserServiceDelegator;
    this.handlerExceptionResolver = handlerExceptionResolver;
  }

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
        .baseUri(AUTH_API_URI + "/login");

    httpSecurity
        .exceptionHandling()
        .accessDeniedHandler(getAccessDeniedHandler())
        .authenticationEntryPoint(getAuthenticationEntryPoint());

    httpSecurity
        .authorizeRequests(getAuthorizeRequests());

    return httpSecurity.build();
  }

  private Customizer<ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry> getAuthorizeRequests() {
    return urlRegistry -> urlRegistry
        .mvcMatchers(HttpMethod.GET, UserWebAdapter.USER_API_URI + "/*")
        .permitAll()
        .mvcMatchers(HttpMethod.GET, PostWebAdapter.POST_API_URI + "/*")
        .permitAll()
        .anyRequest()
        .authenticated();
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

}
