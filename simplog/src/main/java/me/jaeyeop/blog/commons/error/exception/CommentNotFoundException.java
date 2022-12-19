package me.jaeyeop.blog.commons.error.exception;

import me.jaeyeop.blog.commons.error.Error;

/**
 * @author jaeyeopme Created on 10/20/2022.
 */
public class CommentNotFoundException extends AbstractBaseException {

  public CommentNotFoundException() {
    super(Error.COMMENT_NOT_FOUND);
  }

}
