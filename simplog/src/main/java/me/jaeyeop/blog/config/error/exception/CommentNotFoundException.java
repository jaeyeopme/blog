package me.jaeyeop.blog.config.error.exception;

import me.jaeyeop.blog.config.error.Error;

public class CommentNotFoundException extends AbstractException {

  public CommentNotFoundException() {
    super(Error.COMMENT_NOT_FOUND);
  }

}
