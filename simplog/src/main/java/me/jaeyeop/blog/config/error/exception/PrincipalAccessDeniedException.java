package me.jaeyeop.blog.config.error.exception;

import me.jaeyeop.blog.config.error.Error;

public class PrincipalAccessDeniedException extends AbstractException {

  public PrincipalAccessDeniedException() {
    super(Error.FORBIDDEN);
  }

}
