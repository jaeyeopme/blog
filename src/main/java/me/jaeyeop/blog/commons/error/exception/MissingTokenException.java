package me.jaeyeop.blog.commons.error.exception;

import me.jaeyeop.blog.commons.error.Error;
import me.jaeyeop.blog.commons.token.TokenType;

public class MissingTokenException extends AbstractBaseException {
    public MissingTokenException(final TokenType tokenType) {
        super(Error.MISSING_TOKEN, String.format("%s token is missing.", tokenType.name()));
    }
}
