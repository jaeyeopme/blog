package me.jaeyeop.blog.config.error.exception;

import me.jaeyeop.blog.config.error.ErrorCode;

public class CommentNotFoundException extends AbstractException {

  public CommentNotFoundException() {
    super(ErrorCode.COMMENT_NOT_FOUND);
  }

}
