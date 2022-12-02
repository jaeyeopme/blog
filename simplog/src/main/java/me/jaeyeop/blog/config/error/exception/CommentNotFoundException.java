package me.jaeyeop.blog.config.error.exception;

import me.jaeyeop.blog.config.error.Error;

/**
 * @author jaeyeopme Created on 10/20/2022.
 */
public class CommentNotFoundException extends AbstractBaseException {

  public CommentNotFoundException() {
    super(Error.COMMENT_NOT_FOUND);
  }

}
