package me.jaeyeop.blog.config.error.exception;

import me.jaeyeop.blog.config.error.Error;

/**
 * @author jaeyeopme Created on 10/12/2022.
 */
public class PostNotFoundException extends AbstractBaseException {

  public PostNotFoundException() {
    super(Error.POST_NOT_FOUND);
  }

}
