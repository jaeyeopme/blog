package me.jaeyeop.blog.common.error.exception;

import lombok.Getter;
import me.jaeyeop.blog.common.error.ErrorCode;

@Getter
public abstract class BlogException extends RuntimeException {

  private final ErrorCode code;

  protected BlogException(final ErrorCode code) {
    super(code.getMessage());
    this.code = code;
  }

  @Override
  public synchronized Throwable fillInStackTrace() {
    return this;
  }

}
