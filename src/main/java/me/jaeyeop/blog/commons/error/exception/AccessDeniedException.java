package me.jaeyeop.blog.commons.error.exception;

import me.jaeyeop.blog.commons.error.Error;

/**
 * @author jaeyeopme Created on 10/14/2022.
 */
public class AccessDeniedException extends AbstractBaseException {

  public AccessDeniedException() {
    super(Error.FORBIDDEN);
  }

}
