package me.jaeyeop.blog.config.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import me.jaeyeop.blog.authentication.adapter.in.AuthenticationWebAdaptor;
import me.jaeyeop.blog.comment.adapter.in.CommentWebAdapter;
import me.jaeyeop.blog.config.security.authentication.OAuth2AuthenticationFilter;
import me.jaeyeop.blog.config.security.authentication.OAuth2SuccessHandler;
import me.jaeyeop.blog.config.security.authentication.OAuth2UserServiceDelegator;
import me.jaeyeop.blog.post.adapter.in.PostWebAdapter;
import me.jaeyeop.blog.user.adapter.in.UserWebAdapter;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
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
        .headers().frameOptions().disable()
        .and()
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
        .baseUri(AuthenticationWebAdaptor.AUTHENTICATION_API_URI);

    httpSecurity
        .exceptionHandling()
        .accessDeniedHandler(getAccessDeniedHandler())
        .authenticationEntryPoint(getAuthenticationEntryPoint());

    httpSecurity
        .authorizeRequests(getAuthorizeRequests());

    return httpSecurity.build();
  }

  private Customizer<ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry> getAuthorizeRequests() {
    final var openApi = new String[]{"/swagger-ui/**", "/api-docs/**"};
    final var permitAll = new String[]{
        UserWebAdapter.USER_API_URI + "/*",
        PostWebAdapter.POST_API_URI + "/**",
        CommentWebAdapter.COMMENT_API_URI + "/**"};

    return urlRegistry -> urlRegistry
        .requestMatchers(
            PathRequest.toStaticResources().atCommonLocations())
        .permitAll()
        .antMatchers(HttpMethod.GET, openApi)
        .permitAll()
        .antMatchers(HttpMethod.GET, permitAll)
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
