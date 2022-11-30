package me.jaeyeop.blog.config.error.exception;

import lombok.Getter;
import me.jaeyeop.blog.config.error.Error;

@Getter
public abstract class AbstractException extends RuntimeException {

  private final Error code;

  protected AbstractException(final Error code) {
    super(code.message());
    this.code = code;
  }

  @Override
  public synchronized Throwable fillInStackTrace() {
    return this;
  }

}
