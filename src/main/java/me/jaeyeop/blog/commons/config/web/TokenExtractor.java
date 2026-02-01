package me.jaeyeop.blog.commons.config.web;

import jakarta.servlet.http.HttpServletRequest;
import me.jaeyeop.blog.commons.token.TokenType;

public interface TokenExtractor {
    String extract(HttpServletRequest request, TokenType tokenType);
}
