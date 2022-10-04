package me.jaeyeop.blog.config.error.exception;

import lombok.Getter;
import me.jaeyeop.blog.config.error.ErrorCode;

@Getter
public abstract class AbstractException extends RuntimeException {

  private final ErrorCode code;

  protected AbstractException(final ErrorCode code) {
    super(code.getMessage());
    this.code = code;
  }

  @Override
  public synchronized Throwable fillInStackTrace() {
    return this;
  }

}
