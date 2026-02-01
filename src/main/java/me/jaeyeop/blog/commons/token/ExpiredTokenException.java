package me.jaeyeop.blog.commons.token;

import me.jaeyeop.blog.authentication.domain.Token;
import org.springframework.security.authentication.CredentialsExpiredException;

public class ExpiredTokenException extends CredentialsExpiredException {
    private final transient Token token;

    public ExpiredTokenException(final Token token) {
        super("Expired token");
        this.token = token;
    }

    public Token token() {
        return token;
    }
}
