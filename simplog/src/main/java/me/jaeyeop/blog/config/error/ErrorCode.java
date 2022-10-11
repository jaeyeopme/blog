package me.jaeyeop.blog.config.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

  INVALID_ARGUMENT("잘못된 입력값입니다", HttpStatus.BAD_REQUEST),

  UNAUTHORIZED("유효하지 않은 토큰입니다.", HttpStatus.UNAUTHORIZED),

  FORBIDDEN("접근 권한이 없습니다.", HttpStatus.FORBIDDEN),

  EMAIL_NOT_FOUND("존재하지 않는 회원입니다.", HttpStatus.NOT_FOUND);

  private final String message;

  private final HttpStatus status;

  ErrorCode(final String message,
      final HttpStatus status) {
    this.message = message;
    this.status = status;
  }

}
