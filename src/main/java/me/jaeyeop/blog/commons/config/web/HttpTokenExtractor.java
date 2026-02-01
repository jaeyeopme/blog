package me.jaeyeop.blog.commons.config.web;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import me.jaeyeop.blog.commons.token.TokenType;

import static me.jaeyeop.blog.commons.token.TokenProvider.BEARER_TYPE;

@Component
public class HttpTokenExtractor implements TokenExtractor {
    public static final String REFRESH_AUTHORIZATION_HEADER = "Refresh-Authorization";

    @Override
    public String extract(final HttpServletRequest request, final TokenType tokenType) {
        final var headerName = getHeaderName(tokenType);
        final var header = request.getHeader(headerName);

        if (StringUtils.hasText(header) && header.startsWith(BEARER_TYPE)) {
            return header.substring(BEARER_TYPE.length());
        }

        return header;
    }

    private String getHeaderName(final TokenType tokenType) {
        if (tokenType == TokenType.REFRESH) {
            return REFRESH_AUTHORIZATION_HEADER;
        }
        return HttpHeaders.AUTHORIZATION;
    }
}
