package me.jaeyeop.blog.commons.config.web;

import java.util.Objects;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.core.MethodParameter;
import org.springframework.lang.NonNull;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import me.jaeyeop.blog.commons.error.exception.MissingTokenException;

public class ExtractTokenArgumentResolver implements HandlerMethodArgumentResolver {
    private final TokenExtractor tokenExtractor;

    public ExtractTokenArgumentResolver(final TokenExtractor tokenExtractor) {
        this.tokenExtractor = tokenExtractor;
    }

    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        return parameter.hasParameterAnnotation(ExtractToken.class)
                && parameter.getParameterType().equals(String.class);
    }

    @Override
    public Object resolveArgument(
            @NonNull
            final MethodParameter parameter,
            final ModelAndViewContainer mavContainer,
            @NonNull
            final NativeWebRequest webRequest,
            final WebDataBinderFactory binderFactory) {
        final var annotation =
                Objects.requireNonNull(parameter.getParameterAnnotation(ExtractToken.class));
        final var request = webRequest.getNativeRequest(HttpServletRequest.class);

        final var token = tokenExtractor.extract(request, annotation.type());
        if (StringUtils.hasText(token)) {
            return token;
        }

        throw new MissingTokenException(annotation.type());
    }
}
