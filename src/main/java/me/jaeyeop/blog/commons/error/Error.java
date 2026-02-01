package me.jaeyeop.blog.commons.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum Error {
    INVALID_ARGUMENT("Invalid argument", HttpStatus.BAD_REQUEST),

    MISSING_TOKEN("Token is missing", HttpStatus.BAD_REQUEST),

    UNAUTHORIZED("Invalid token", HttpStatus.UNAUTHORIZED),

    FORBIDDEN("Access denied", HttpStatus.FORBIDDEN),

    USER_NOT_FOUND("User not found", HttpStatus.NOT_FOUND),

    POST_NOT_FOUND("Post not found", HttpStatus.NOT_FOUND),

    COMMENT_NOT_FOUND("Comment not found", HttpStatus.NOT_FOUND),

    OAUTH_PROVIDER_MISMATCH(
            "Email already registered. Please login with your social account.",
            HttpStatus.BAD_REQUEST),

    INTERNAL_SERVER_ERROR(
            "Server error. Please try again later.", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String message;

    private final HttpStatus status;

    Error(final String message, final HttpStatus status) {
        this.message = message;
        this.status = status;
    }
}
