package me.jaeyeop.blog.config.error.exception;

import me.jaeyeop.blog.config.error.ErrorCode;

public class EmailNotFoundException extends AbstractException {

  public EmailNotFoundException() {
    super(ErrorCode.USER_NOT_FOUND);
  }

}
