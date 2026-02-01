package me.jaeyeop.blog.commons.error.exception;

import me.jaeyeop.blog.commons.error.Error;

public class OAuth2ProviderMismatchException extends AbstractBaseException {
    private final String message;

    public OAuth2ProviderMismatchException(final String provider) {
        super(Error.OAUTH_PROVIDER_MISMATCH);
        this.message =
                String.format("Email already registered. Please login with %s account.", provider);
    }

    @Override
    public String getMessage() {
        return message;
    }
}
