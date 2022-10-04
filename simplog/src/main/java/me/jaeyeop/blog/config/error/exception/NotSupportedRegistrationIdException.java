package me.jaeyeop.blog.config.error.exception;

import me.jaeyeop.blog.config.error.ErrorCode;

public class NotSupportedRegistrationIdException extends AbstractException {

  public NotSupportedRegistrationIdException() {
    super(ErrorCode.UNAUTHORIZED);
  }

}
