package me.jaeyeop.blog.config.error.exception;

import me.jaeyeop.blog.config.error.Error;

public class NotSupportedRegistrationIdException extends AbstractException {

  public NotSupportedRegistrationIdException() {
    super(Error.UNAUTHORIZED);
  }

}
