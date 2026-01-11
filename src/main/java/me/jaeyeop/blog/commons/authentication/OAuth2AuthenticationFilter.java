package me.jaeyeop.blog.commons.authentication;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import me.jaeyeop.blog.authentication.application.port.out.ExpiredTokenQueryPort;
import me.jaeyeop.blog.authentication.domain.Token;
import me.jaeyeop.blog.commons.config.security.UserPrincipal;
import me.jaeyeop.blog.commons.token.TokenProvider;
import me.jaeyeop.blog.user.application.port.out.UserQueryPort;
import me.jaeyeop.blog.user.domain.User;
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
public class OAuth2AuthenticationFilter extends OncePerRequestFilter {
    private final UserQueryPort userQueryPort;
    private final ExpiredTokenQueryPort expiredTokenQueryPort;
    private final TokenProvider tokenProvider;

    public OAuth2AuthenticationFilter(
            final UserQueryPort userQueryPort,
            final ExpiredTokenQueryPort expiredTokenQueryPort,
            final TokenProvider tokenProvider) {
        this.userQueryPort = userQueryPort;
        this.expiredTokenQueryPort = expiredTokenQueryPort;
        this.tokenProvider = tokenProvider;
    }

    @Override
    protected void doFilterInternal(
            @NonNull final HttpServletRequest request,
            @NonNull final HttpServletResponse response,
            @NonNull final FilterChain chain)
            throws ServletException, IOException {
        final var token = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (StringUtils.hasText(token)) {
            try {
                final var authResult = attemptAuthentication(request, token);
                successfulAuthentication(authResult);
            } catch (final AuthenticationException e) {
                unsuccessfulAuthentication(e);
            }
        }

        chain.doFilter(request, response);
    }

    private Authentication attemptAuthentication(
            final HttpServletRequest request, final String token) {
        final var acessToken = obtainToken(token);
        final var user = retrieveUser(acessToken.email());

        return createSuccessAuthentication(request, UserPrincipal.from(user));
    }

    private Token obtainToken(final String token) {
        final var verifiedToken = tokenProvider.verify(token);

        if (expiredTokenQueryPort.isExpired(verifiedToken.value())) {
            throw new BadCredentialsException("expired access token");
        }

        return verifiedToken;
    }

    private User retrieveUser(final String email) {
        return userQueryPort
                .findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("user not found"));
    }

    private Authentication createSuccessAuthentication(
            final HttpServletRequest request, final UserPrincipal principal) {
        final var auth =
                UsernamePasswordAuthenticationToken.authenticated(
                        principal, Strings.EMPTY, principal.getAuthorities());
        auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        return auth;
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
