package me.jaeyeop.blog.commons.error.exception;

import me.jaeyeop.blog.commons.error.Error;

public class CommentNotFoundException extends AbstractBaseException {

    public CommentNotFoundException() {
        super(Error.COMMENT_NOT_FOUND);
    }
}
