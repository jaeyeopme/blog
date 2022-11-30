package me.jaeyeop.blog.config.error.exception;

import me.jaeyeop.blog.config.error.Error;

public class PostNotFoundException extends AbstractException {

  public PostNotFoundException() {
    super(Error.POST_NOT_FOUND);
  }

}
