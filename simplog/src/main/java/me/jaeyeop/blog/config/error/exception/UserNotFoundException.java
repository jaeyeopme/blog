package me.jaeyeop.blog.config.error.exception;

import me.jaeyeop.blog.config.error.Error;

/**
 * @author jaeyeopme Created on 10/06/2022.
 */
public class UserNotFoundException extends AbstractBaseException {

  public UserNotFoundException() {
    super(Error.USER_NOT_FOUND);
  }

}
