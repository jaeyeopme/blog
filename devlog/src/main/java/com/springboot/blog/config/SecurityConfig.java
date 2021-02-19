package com.springboot.blog.config;

import com.springboot.blog.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    public static final String NOT_FOUND_MESSAGE_FORMAT = "Not found %s.";
    public static final String NULL_MESSAGE_FORMAT = "%s cannot be <strong>null</strong>.";

    public static final String EXISTS_EMAIL_MESSAGE = "Email <strong>%s</strong> is already taken.";
    public static final String EMAIL_REQUEST = "email";

    public static final String JWT_TOKEN_PREFIX = "Bearer ";

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String INVALID_TOKEN_MESSAGE = "Invalid token.";

    public static final long EXPIRATION_TIME_10_MINUTES = 60000 * 10;
    public static final long EXPIRATION_TIME_1_DAYS = 60000 * 1440;

    public static final String LOGIN_SUBJECT = "login subject";
    public static final String SIGNUP_SUBJECT = "signup subject";

    public static final String SIGNUP_LINK = "http://localhost:8080/signup/confirm?token=%s";
    public static final String LOGIN_LINK = "http://localhost:8080/login/confirm?token=%s";

    public static final String ENCODING = "UTF-8";
    public static final String INVALID_EMAIL_MESSAGE = "Invalid email.";

    public static final String SEND_SUCCESS_MESSAGE = "Email send success.";
    public static final String SEND_FAILED_MESSAGE = "Failed to send email.";

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterBefore(jwtAuthenticationFilter, BasicAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers("/signup/**", "/login/**")
                .permitAll()
                .antMatchers(HttpMethod.GET, "/", "/board/*")
                .permitAll()
                .anyRequest()
                .authenticated();
    }

}
