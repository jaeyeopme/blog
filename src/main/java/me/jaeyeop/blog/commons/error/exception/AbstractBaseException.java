package me.jaeyeop.blog.commons.error.exception;

import lombok.Getter;
import me.jaeyeop.blog.commons.error.Error;

@Getter
public abstract class AbstractBaseException extends RuntimeException {
    private final Error code;

    protected AbstractBaseException(final Error code) {
        super(code.message());
        this.code = code;
    }

    protected AbstractBaseException(final Error code, final String message) {
        super(message);
        this.code = code;
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
