package me.jaeyeop.blog.commons.error.exception;

import me.jaeyeop.blog.commons.error.Error;

public class UserNotFoundException extends AbstractBaseException {
    public UserNotFoundException() {
        super(Error.USER_NOT_FOUND);
    }
}
