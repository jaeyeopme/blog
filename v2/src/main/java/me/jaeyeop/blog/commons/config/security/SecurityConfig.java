package me.jaeyeop.blog.commons.config.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import me.jaeyeop.blog.authentication.adapter.in.AuthenticationWebAdaptor;
import me.jaeyeop.blog.comment.adapter.in.CommentWebAdapter;
import me.jaeyeop.blog.commons.authentication.OAuth2AuthenticationFilter;
import me.jaeyeop.blog.commons.authentication.OAuth2SuccessHandler;
import me.jaeyeop.blog.commons.authentication.OAuth2UserServiceDelegator;
import me.jaeyeop.blog.post.adapter.in.PostWebAdapter;
import me.jaeyeop.blog.user.adapter.in.UserWebAdapter;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

/**
 * @author jaeyeopme Created on 09/26/2022.
 */
@Configuration
public class SecurityConfig {

  private final OAuth2AuthenticationFilter oAuth2AuthenticationFilter;

  private final OAuth2SuccessHandler oAuth2SuccessHandler;

  private final OAuth2UserServiceDelegator oAuth2UserServiceDelegator;

  private final HandlerExceptionResolver handlerExceptionResolver;

  public SecurityConfig(
      final OAuth2AuthenticationFilter oAuth2AuthenticationFilter,
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
    final var oas = new String[]{"/swagger-ui/**", "/api-docs/**"};
    final var endpoint = new String[]{
        UserWebAdapter.USER_API_URI + "/*",
        PostWebAdapter.POST_API_URI + "/**",
        CommentWebAdapter.COMMENT_API_URI + "/**"};

    httpSecurity
        .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
        .csrf(AbstractHttpConfigurer::disable)
        .cors(AbstractHttpConfigurer::disable)
        .formLogin(AbstractHttpConfigurer::disable)
        .httpBasic(AbstractHttpConfigurer::disable)
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .addFilterAt(oAuth2AuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
        .oauth2Login(oauth2 -> oauth2
            .successHandler(oAuth2SuccessHandler)
            .userInfoEndpoint(userInfo -> userInfo.userService(oAuth2UserServiceDelegator))
            .authorizationEndpoint(authorization -> authorization
                .baseUri(AuthenticationWebAdaptor.AUTHENTICATION_API_URI))
        )
        .exceptionHandling(exceptions -> exceptions
            .accessDeniedHandler(getAccessDeniedHandler())
            .authenticationEntryPoint(getAuthenticationEntryPoint())
        )
        .authorizeHttpRequests(auth -> auth
            .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
            .requestMatchers(HttpMethod.GET, oas).permitAll()
            .requestMatchers(HttpMethod.GET, endpoint).permitAll()
            .anyRequest().authenticated()
        );

    return httpSecurity.build();
  }

  private AuthenticationEntryPoint getAuthenticationEntryPoint() {
    return this::resolveException;
  }

  private AccessDeniedHandler getAccessDeniedHandler() {
    return this::resolveException;
  }

  private void resolveException(
      final HttpServletRequest request,
      final HttpServletResponse response,
      final RuntimeException exception) {
    handlerExceptionResolver.resolveException(request, response, null, exception);
  }

}
