package com.springboot.blog.jwt;

import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

import static com.springboot.blog.config.SecurityConfig.AUTHORIZATION_HEADER;
import static com.springboot.blog.config.SecurityConfig.JWT_TOKEN_PREFIX;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String accessToken = request.getHeader(AUTHORIZATION_HEADER);

        if (accessToken != null && accessToken.startsWith(JWT_TOKEN_PREFIX)) {
            DecodedJWT decodedToken = jwtService.getDecodedToken(accessToken);

            SecurityContextHolder.getContext().setAuthentication(getAuthentication(request, decodedToken));
        }

        chain.doFilter(request, response);
    }

    private Authentication getAuthentication(HttpServletRequest request, DecodedJWT decodedToken) {
        String email = jwtService.getAudience(decodedToken);
        Collection<? extends GrantedAuthority> authorities = jwtService.getAuthorities(decodedToken);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(email, null, authorities);
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        return authentication;
    }



}
