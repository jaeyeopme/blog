package me.jaeyeop.blog.config.error.exception;

import me.jaeyeop.blog.config.error.ErrorCode;

public class PostNotFoundException extends AbstractException {

  public PostNotFoundException() {
    super(ErrorCode.POST_NOT_FOUND);
  }

}
