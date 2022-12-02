package me.jaeyeop.blog.config.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * @author jaeyeopme Created on 09/30/2022.
 */
@Getter
public enum Error {

  INVALID_ARGUMENT("잘못된 입력값입니다", HttpStatus.BAD_REQUEST),

  UNAUTHORIZED("유효하지 않은 토큰입니다.", HttpStatus.UNAUTHORIZED),

  FORBIDDEN("접근 권한이 없습니다.", HttpStatus.FORBIDDEN),

  USER_NOT_FOUND("존재하지 않는 사용자입니다.", HttpStatus.NOT_FOUND),

  POST_NOT_FOUND("존재하지 않는 게시글입니다.", HttpStatus.NOT_FOUND),

  COMMENT_NOT_FOUND("존재하지 않는 댓글입니다.", HttpStatus.NOT_FOUND);

  private final String message;

  private final HttpStatus status;

  Error(final String message,
      final HttpStatus status) {
    this.message = message;
    this.status = status;
  }

}
