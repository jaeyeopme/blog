package me.jaeyeop.blog.commons.authentication;

import java.io.IOException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

import me.jaeyeop.blog.commons.error.exception.AbstractBaseException;

@Component
public class OAuth2FailureHandler implements AuthenticationFailureHandler {
    private final HandlerExceptionResolver handlerExceptionResolver;

    public OAuth2FailureHandler(final HandlerExceptionResolver handlerExceptionResolver) {
        this.handlerExceptionResolver = handlerExceptionResolver;
    }

    @Override
    public void onAuthenticationFailure(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final AuthenticationException exception)
            throws IOException {
        if (exception.getCause() instanceof AbstractBaseException e) {
            handlerExceptionResolver.resolveException(request, response, null, e);
            return;
        }

        handlerExceptionResolver.resolveException(request, response, null, exception);
    }
}
