package me.jaeyeop.blog.commons.config.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import me.jaeyeop.blog.authentication.adapter.in.AuthenticationWebAdaptor;
import me.jaeyeop.blog.comment.adapter.in.CommentWebAdapter;
import me.jaeyeop.blog.commons.authentication.OAuth2AuthenticationFilter;
import me.jaeyeop.blog.commons.authentication.OAuth2FailureHandler;
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

@Configuration
public class SecurityConfig {
    private final OAuth2AuthenticationFilter oAuth2AuthenticationFilter;

    private final OAuth2SuccessHandler oAuth2SuccessHandler;

    private final OAuth2FailureHandler oAuth2FailureHandler;

    private final OAuth2UserServiceDelegator oAuth2UserServiceDelegator;

    private final HandlerExceptionResolver handlerExceptionResolver;

    public SecurityConfig(
            final OAuth2AuthenticationFilter oAuth2AuthenticationFilter,
            final OAuth2SuccessHandler oAuth2SuccessHandler,
            final OAuth2FailureHandler oAuth2FailureHandler,
            final OAuth2UserServiceDelegator oAuth2UserServiceDelegator,
            final HandlerExceptionResolver handlerExceptionResolver) {
        this.oAuth2AuthenticationFilter = oAuth2AuthenticationFilter;
        this.oAuth2SuccessHandler = oAuth2SuccessHandler;
        this.oAuth2FailureHandler = oAuth2FailureHandler;
        this.oAuth2UserServiceDelegator = oAuth2UserServiceDelegator;
        this.handlerExceptionResolver = handlerExceptionResolver;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(final HttpSecurity httpSecurity)
            throws Exception {
        final var oas = new String[] {"/swagger-ui/**", "/api-docs/**"};

        // Endpoints that do not require authentication (login, token refresh)
        final var authenticationEndpoints =
                new String[] {
                    AuthenticationWebAdaptor.AUTHENTICATION_API_URI + "/login/**",
                    AuthenticationWebAdaptor.AUTHENTICATION_API_URI + "/refresh"
                };

        // Endpoints that allow only GET requests (posts, comments, user profiles)
        final var publicReadEndpoints =
                new String[] {
                    PostWebAdapter.POST_API_URI + "/**",
                    CommentWebAdapter.COMMENT_API_URI + "/**",
                    UserWebAdapter.USER_API_URI + "/*"
                };

        httpSecurity
                .headers(
                        headers ->
                                headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .sessionManagement(
                        session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterAt(oAuth2AuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .oauth2Login(
                        oauth2 ->
                                oauth2.successHandler(oAuth2SuccessHandler)
                                        .failureHandler(oAuth2FailureHandler)
                                        .userInfoEndpoint(
                                                userInfoEndpointConfig ->
                                                        userInfoEndpointConfig.userService(
                                                                oAuth2UserServiceDelegator))
                                        .authorizationEndpoint(
                                                authorizationEndpoint ->
                                                        authorizationEndpoint.baseUri(
                                                                AuthenticationWebAdaptor
                                                                                .AUTHENTICATION_API_URI
                                                                        + "/login")))
                .exceptionHandling(
                        exceptionHandling ->
                                exceptionHandling
                                        .accessDeniedHandler(getAccessDeniedHandler())
                                        .authenticationEntryPoint(getAuthenticationEntryPoint()))
                .authorizeHttpRequests(
                        auth ->
                                auth.requestMatchers(
                                                PathRequest.toStaticResources().atCommonLocations())
                                        .permitAll()
                                        .requestMatchers(HttpMethod.GET, oas)
                                        .permitAll()
                                        // 1. Allow all methods for authentication-related endpoints
                                        .requestMatchers(authenticationEndpoints)
                                        .permitAll()
                                        // 2. Authentication required for retrieving own profile
                                        // (must have higher priority than publicReadEndpoints
                                        // wildcard)
                                        .requestMatchers(
                                                HttpMethod.GET, UserWebAdapter.USER_API_URI + "/me")
                                        .authenticated()
                                        // 3. Allow only GET requests for public read endpoints
                                        .requestMatchers(HttpMethod.GET, publicReadEndpoints)
                                        .permitAll()
                                        // 4. Authentication required for all other requests
                                        .anyRequest()
                                        .authenticated());

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
