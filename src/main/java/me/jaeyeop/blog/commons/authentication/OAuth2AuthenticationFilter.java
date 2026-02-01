package me.jaeyeop.blog.commons.authentication;

import static me.jaeyeop.blog.commons.token.TokenProvider.BEARER_TYPE;

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
import org.apache.logging.log4j.util.Strings;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
public class OAuth2AuthenticationFilter extends OncePerRequestFilter {
    private final ExpiredTokenQueryPort expiredTokenQueryPort;

    private final TokenProvider tokenProvider;

    public OAuth2AuthenticationFilter(
            final ExpiredTokenQueryPort expiredTokenQueryPort, final TokenProvider tokenProvider) {
        this.expiredTokenQueryPort = expiredTokenQueryPort;
        this.tokenProvider = tokenProvider;
    }

    @Override
    protected void doFilterInternal(
            @NonNull
            final HttpServletRequest request,
            @NonNull
            final HttpServletResponse response,
            @NonNull
            final FilterChain chain)
            throws ServletException, IOException {
        final var header = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (StringUtils.hasText(header) && header.startsWith(BEARER_TYPE)) {
            try {
                final var token = header.substring(BEARER_TYPE.length());
                final var authResult = attemptAuthentication(request, token);
                successfulAuthentication(authResult);
            } catch (final AuthenticationException e) {
                unsuccessfulAuthentication(request, e);
            }
        }

        chain.doFilter(request, response);
    }

    private Authentication attemptAuthentication(
            final HttpServletRequest request, final String token) {
        final var accessToken = obtainToken(token);
        final var claims = accessToken.tokenClaims();
        final var principal = new UserPrincipal(claims.id(), claims.authorities());

        return createSuccessAuthentication(request, principal);
    }

    private Token obtainToken(final String token) {
        final var verifiedToken = tokenProvider.verify(token);

        if (expiredTokenQueryPort.isInvalidated(verifiedToken.value())) {
            throw new CredentialsExpiredException("Expired or invalidated token");
        }

        return verifiedToken;
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

    private void unsuccessfulAuthentication(
            final HttpServletRequest request, final AuthenticationException failed) {
        SecurityContextHolder.clearContext();
        log.trace("Failed to process authentication request", failed);
        log.trace("Cleared SecurityContextHolder");
        log.trace("Handling authentication failure");
        request.setAttribute("exception", failed);
    }
}
