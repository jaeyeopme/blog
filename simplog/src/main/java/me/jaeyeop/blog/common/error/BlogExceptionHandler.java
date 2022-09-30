package me.jaeyeop.blog.common.error;

import java.nio.file.AccessDeniedException;
import javax.naming.AuthenticationException;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import me.jaeyeop.blog.common.error.exception.BlogException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Slf4j
@RestControllerAdvice
public class BlogExceptionHandler {

  private static final String LOG_FORMAT = "Class Name: {}, Message: {}";

  /**
   * 개발자 정의 예외 처리
   *
   * @param e BlogException 을 상속받은 예외
   * @return {@link ErrorCode}
   */
  @SneakyThrows
  @ExceptionHandler(BlogException.class)
  public ResponseEntity<ErrorResponse> blogExceptionHandler(
      final BlogException e) {
    logInfo(e);
    return ErrorResponse.of(e.getCode());
  }

  /**
   * 유효하지 않은 인증 정보 예외 처리
   *
   * @param e 유효하지 않은 인증 정보의 요청에 대한 예외
   * @return {@link ErrorCode} UNAUTHORIZED
   */
  @SneakyThrows
  @ExceptionHandler(AuthenticationException.class)
  public ResponseEntity<ErrorResponse> authenticationExceptionHandler(
      final AuthenticationException e) {
    logInfo(e);
    return ErrorResponse.of(ErrorCode.UNAUTHORIZED);
  }

  /**
   * 접근 권한 예외 처리
   *
   * @param e 권한이 없는 요청에 대한 예외
   * @return {@link ErrorCode} FORBIDDEN
   */
  @SneakyThrows
  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<ErrorResponse> accessDeniedExceptionHandler(
      final AccessDeniedException e) {
    logInfo(e);
    return ErrorResponse.of(ErrorCode.FORBIDDEN);
  }

  /**
   * 데이터 바인딩 에러 예외 처리
   *
   * @param e {@link RequestBody}, {@link RequestPart} 를 제외한 {@link Valid} 주석이 달린 인수 바인딩 예외
   * @return 바인딩 에러 필드를 포함한 HTTP 400 BAD_REQUEST
   */
  @SneakyThrows
  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ErrorResponse> constraintViolationExceptionHandler(
      final ConstraintViolationException e) {
    logInfo(e);
    return ErrorResponse.of(e.getConstraintViolations());
  }

  /**
   * 데이터 바인딩 에러 예외 처리
   *
   * @param e {@link ModelAttribute} 와 {@link Valid} 주석이 달린 인수에서 바인딩 예외
   * @return 바인딩 에러 필드를 포함한 HTTP 400 BAD_REQUEST
   */
  @SneakyThrows
  @ExceptionHandler(BindException.class)
  public ResponseEntity<ErrorResponse> bindExceptionHandler(
      final BindException e) {
    logInfo(e);
    return ErrorResponse.of(e.getBindingResult());
  }

  /**
   * 데이터 바인딩 에러 예외 처리
   *
   * @param e {@link RequestParam} 주석이 달린 인수에서 enum 바인딩 예외
   * @return HTTP 400 BAD_REQUEST
   */
  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<Void> methodArgumentTypeMismatchExceptionHandler(
      final MethodArgumentTypeMismatchException e) {
    logInfo(e);
    return ResponseEntity.badRequest().build();
  }

  /**
   * HTTP Method 예외 처리
   *
   * @param e 지원하지 않은 HTTP Method 예외
   * @return HTTP 405 METHOD_NOT_ALLOWED
   */
  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  public ResponseEntity<Void> httpRequestMethodNotSupportedExceptionHandler(
      HttpRequestMethodNotSupportedException e) {
    logInfo(e);
    return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).build();
  }

  private void logInfo(final Exception e) {
    log.info(LOG_FORMAT, e.getClass().getSimpleName(), e.getMessage());
  }

}
