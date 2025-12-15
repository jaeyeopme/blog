package me.jaeyeop.blog.commons.error.exception;

import me.jaeyeop.blog.commons.error.Error;

public class AccessDeniedException extends AbstractBaseException {

    public AccessDeniedException() {
        super(Error.FORBIDDEN);
    }
}
