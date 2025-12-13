package me.jaeyeop.blog.commons.error.exception;

import me.jaeyeop.blog.commons.error.Error;

/**
 * @author jaeyeopme Created on 10/12/2022.
 */
public class PostNotFoundException extends AbstractBaseException {

    public PostNotFoundException() {
        super(Error.POST_NOT_FOUND);
    }
}
