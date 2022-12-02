package me.jaeyeop.blog.config.error.aop;

import lombok.extern.slf4j.Slf4j;
import me.jaeyeop.blog.config.error.Error;
import me.jaeyeop.blog.config.error.ErrorResponse;
import me.jaeyeop.blog.config.error.FieldErrorResponse;
import me.jaeyeop.blog.config.error.exception.AbstractBaseException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * @author jaeyeopme Created on 09/30/2022.
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  private static final String LOG_FORMAT = "Class Name: {}, Message: {}";

  /**
   * 개발자 정의 예외 처리
   *
   * @param e 개발자 정의 예외
   * @return {@link Error}
   */
  @ExceptionHandler(AbstractBaseException.class)
  public ResponseEntity<ErrorResponse> blogExceptionHandler(
      final AbstractBaseException e) {
    logDebug(e);
    return ResponseEntity.status(e.code().status())
        .body(new ErrorResponse(e.code().message()));
  }

  /**
   * 유효하지 않은 인증 정보 예외 처리
   *
   * @param e 유효하지 않은 인증 정보의 요청에 대한 예외
   * @return {@link Error} UNAUTHORIZED
   */
  @ExceptionHandler(AuthenticationException.class)
  public ResponseEntity<ErrorResponse> authenticationExceptionHandler(
      final AuthenticationException e) {
    logError(e);
    return ResponseEntity.status(Error.UNAUTHORIZED.status())
        .body(new ErrorResponse(Error.UNAUTHORIZED.message()));
  }

  /**
   * 접근 권한 예외 처리
   *
   * @param e 권한이 없는 요청에 대한 예외
   * @return {@link Error} FORBIDDEN
   */
  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<ErrorResponse> accessDeniedExceptionHandler(
      final AccessDeniedException e) {
    logError(e);
    return ResponseEntity.status(Error.FORBIDDEN.status())
        .body(new ErrorResponse(Error.FORBIDDEN.message()));
  }

  /**
   * 헤더 바인딩 에러 예외 처리
   *
   * @param e {@link RequestHeader} 바인딩 예외
   * @return {@link Error} INVALID_ARGUMENT
   */
  @ExceptionHandler(MissingRequestHeaderException.class)
  public ResponseEntity<ErrorResponse> missingRequestHeaderExceptionHandler(
      final MissingRequestHeaderException e) {
    logError(e);
    return ResponseEntity.badRequest()
        .body(new ErrorResponse(Error.INVALID_ARGUMENT.message()));
  }

  /**
   * 데이터 바인딩 에러 예외 처리 재정의
   *
   * @param e {@link RequestBody}에서 {@link Validated} 인수 바인딩 예외
   * @return 바인딩 에러 필드를 포함한 HTTP 400 BAD_REQUEST
   */
  @Override
  protected @NonNull ResponseEntity<Object> handleMethodArgumentNotValid(
      @NonNull final MethodArgumentNotValidException e,
      @NonNull final HttpHeaders headers,
      @NonNull final HttpStatus status,
      @NonNull final WebRequest request) {
    logError(e);
    return ResponseEntity.badRequest()
        .body(FieldErrorResponse.from(e.getBindingResult()));
  }

  private void logDebug(final Exception e) {
    log.debug(LOG_FORMAT, e.getClass().getSimpleName(), e.getMessage());
  }

  private void logError(final Exception e) {
    log.error(LOG_FORMAT, e.getClass().getSimpleName(), e.getMessage());
  }

}
