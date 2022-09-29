package me.jaeyeop.blog.config.security;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jaeyeop.blog.user.application.port.out.UserQueryPort;
import org.apache.logging.log4j.util.Strings;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationFilter extends OncePerRequestFilter {

  private static final String BEARER = "Bearer ";

  private final UserQueryPort userQueryPort;

  private final JWTProvider jwtProvider;

  @Override
  protected void doFilterInternal(
      @NonNull final HttpServletRequest request,
      @NonNull final HttpServletResponse response,
      @NonNull final FilterChain chain)
      throws ServletException, IOException {
    try {
      final var authResult = attemptAuthentication(request);
      successfulAuthentication(authResult);
    } catch (final AuthenticationException e) {
      unsuccessfulAuthentication(e);
    }

    chain.doFilter(request, response);
  }

  private Authentication attemptAuthentication(final HttpServletRequest request) {
    final var accessToken = obtainAccessToken(request);
    final var principal = retrieveUser(accessToken);

    return createSuccessAuthentication(request, principal);
  }

  private String obtainAccessToken(final HttpServletRequest httpServletRequest) {
    final var value = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);

    if (isBearerToken(value)) {
      return removeType(value);
    }

    log.debug("Failed to authenticate since no credentials provided");
    throw new BadCredentialsException("Bad credentials");
  }

  private boolean isBearerToken(final String value) {
    return StringUtils.hasText(value) && value.startsWith(OAuth2AuthenticationFilter.BEARER);
  }

  private String removeType(final String value) {
    return value.substring(OAuth2AuthenticationFilter.BEARER.length());
  }

  private OAuth2UserPrincipal retrieveUser(final String accessToken) {
    final var email = getEmail(accessToken);

    if (userQueryPort.existsByEmail(email)) {
      return OAuth2UserPrincipal.from(userQueryPort.findByEmail(email));
    }

    log.debug("Failed to find user '{}'", email);
    throw new UsernameNotFoundException("User not found");
  }

  private String getEmail(final String accessToken) {
    return jwtProvider.getEmail(accessToken)
        .orElseThrow(() -> {
          log.debug("Failed to authenticate since no credentials provided");
          throw new BadCredentialsException("Bad credentials");
        });
  }

  private Authentication createSuccessAuthentication(
      final HttpServletRequest request,
      final OAuth2UserPrincipal principal) {
    final var result = new UsernamePasswordAuthenticationToken(
        principal,
        Strings.EMPTY,
        principal.getAuthorities());
    result.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

    log.debug("Authenticated user");
    return result;
  }

  private void successfulAuthentication(final Authentication authResult) {
    final var context = SecurityContextHolder.createEmptyContext();
    context.setAuthentication(authResult);
    SecurityContextHolder.setContext(context);
    log.debug("Set SecurityContextHolder to {}", authResult);
  }

  private void unsuccessfulAuthentication(final AuthenticationException failed) {
    SecurityContextHolder.clearContext();
    log.trace("Failed to process authentication request", failed);
    log.trace("Cleared SecurityContextHolder");
    log.trace("Handling authentication failure");
  }

}
