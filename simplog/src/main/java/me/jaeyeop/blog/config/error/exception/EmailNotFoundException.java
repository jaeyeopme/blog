package me.jaeyeop.blog.config.error.exception;

import me.jaeyeop.blog.config.error.ErrorCode;

public class EmailNotFoundException extends AbstractException {

  public EmailNotFoundException() {
    super(ErrorCode.EMAIL_NOT_FOUND);
  }

}
