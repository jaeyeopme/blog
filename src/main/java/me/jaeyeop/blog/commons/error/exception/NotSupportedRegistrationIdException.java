package me.jaeyeop.blog.commons.error.exception;

import me.jaeyeop.blog.commons.error.Error;

public class NotSupportedRegistrationIdException extends AbstractBaseException {

    public NotSupportedRegistrationIdException() {
        super(Error.UNAUTHORIZED);
    }
}
