package me.jaeyeop.blog.common.error.exception;

import me.jaeyeop.blog.common.error.ErrorCode;

public class EmailNotFoundException extends BlogException {

  public EmailNotFoundException() {
    super(ErrorCode.USER_NOT_FOUND);
  }

}
