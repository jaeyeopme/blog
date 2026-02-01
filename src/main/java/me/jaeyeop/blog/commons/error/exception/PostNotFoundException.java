package me.jaeyeop.blog.commons.error.exception;

import me.jaeyeop.blog.commons.error.Error;

public class PostNotFoundException extends AbstractBaseException {
    public PostNotFoundException() {
        super(Error.POST_NOT_FOUND);
    }
}
