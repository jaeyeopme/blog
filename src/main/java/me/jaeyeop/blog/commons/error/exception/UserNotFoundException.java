package me.jaeyeop.blog.commons.error.exception;

import me.jaeyeop.blog.commons.error.Error;

/**
 * @author jaeyeopme Created on 10/06/2022.
 */
public class UserNotFoundException extends AbstractBaseException {

    public UserNotFoundException() {
        super(Error.USER_NOT_FOUND);
    }
}
