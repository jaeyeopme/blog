package me.jaeyeop.blog.config.error.exception;

import me.jaeyeop.blog.config.error.ErrorCode;

public class PrincipalAccessDeniedException extends AbstractException {

  public PrincipalAccessDeniedException() {
    super(ErrorCode.FORBIDDEN);
  }

}
