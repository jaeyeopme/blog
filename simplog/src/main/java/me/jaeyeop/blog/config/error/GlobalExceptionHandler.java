package me.jaeyeop.blog.config.error;

import java.nio.file.AccessDeniedException;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import me.jaeyeop.blog.config.error.exception.AbstractException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  private static final String LOG_FORMAT = "Class Name: {}, Message: {}";

  /**
   * 개발자 정의 예외 처리
   *
   * @param e 개발자 정의 예외
   * @return {@link ErrorCode}
   */
  @ExceptionHandler(AbstractException.class)
  public ResponseEntity<ErrorResponse> blogExceptionHandler(
      final AbstractException e) {
    logInfo(e);
    return ErrorResponse.of(e.getCode());
  }

  /**
   * 유효하지 않은 인증 정보 예외 처리
   *
   * @param e 유효하지 않은 인증 정보의 요청에 대한 예외
   * @return {@link ErrorCode} UNAUTHORIZED
   */
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
  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ErrorResponse> constraintViolationExceptionHandler(
      final ConstraintViolationException e) {
    logInfo(e);
    return ErrorResponse.of(e.getConstraintViolations());
  }

  /**
   * 데이터 바인딩 에러 예외 처리 재정의
   *
   * @param e {@link RequestBody} 에서 {@link Valid} 주석이 달린 인수 바인딩 예외
   * @return 바인딩 에러 필드를 포함한 HTTP 400 BAD_REQUEST
   */
  @Override
  protected @NonNull ResponseEntity<Object> handleMethodArgumentNotValid(
      @NonNull final MethodArgumentNotValidException e,
      @NonNull final HttpHeaders headers,
      @NonNull final HttpStatus status,
      @NonNull final WebRequest request) {
    logInfo(e);

    return ErrorResponse.of(e.getBindingResult());
  }

  private void logInfo(final Exception e) {
    log.debug(LOG_FORMAT, e.getClass().getSimpleName(), e.getMessage());
  }

}
