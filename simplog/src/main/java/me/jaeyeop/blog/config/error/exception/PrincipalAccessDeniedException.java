package me.jaeyeop.blog.config.error.exception;

import me.jaeyeop.blog.config.error.Error;

/**
 * @author jaeyeopme Created on 10/14/2022.
 */
public class PrincipalAccessDeniedException extends AbstractBaseException {

  public PrincipalAccessDeniedException() {
    super(Error.FORBIDDEN);
  }

}
