package me.jaeyeop.blog.config.error.exception;

import me.jaeyeop.blog.config.error.Error;

public class EmailNotFoundException extends AbstractException {

  public EmailNotFoundException() {
    super(Error.EMAIL_NOT_FOUND);
  }

}
