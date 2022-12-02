package me.jaeyeop.blog.config.error.exception;

import me.jaeyeop.blog.config.error.Error;

/**
 * @author jaeyeopme Created on 09/29/2022.
 */
public class NotSupportedRegistrationIdException extends AbstractBaseException {

  public NotSupportedRegistrationIdException() {
    super(Error.UNAUTHORIZED);
  }

}
