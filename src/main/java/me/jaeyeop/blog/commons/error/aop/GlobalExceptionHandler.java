package me.jaeyeop.blog.commons.error.aop;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import me.jaeyeop.blog.commons.error.Error;
import me.jaeyeop.blog.commons.error.ErrorResponse;
import me.jaeyeop.blog.commons.error.FieldErrorResponse;
import me.jaeyeop.blog.commons.error.exception.AbstractBaseException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public final class GlobalExceptionHandler {
    private static final String LOG_FORMAT = "Class Name: {}, Message: {}";

    /**
     * Handle custom exceptions defined by developers
     *
     * @param e Custom exception
     * @return {@link Error}
     */
    @ExceptionHandler(AbstractBaseException.class)
    public ResponseEntity<ErrorResponse> blogExceptionHandler(final AbstractBaseException e) {
        logWarn(e);
        return ResponseEntity.status(e.code().status()).body(new ErrorResponse(e.getMessage()));
    }

    /**
     * Handle invalid authentication information exceptions
     *
     * @param e Exception for request with invalid authentication info
     * @return {@link Error} UNAUTHORIZED
     */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> authenticationExceptionHandler(
            final AuthenticationException e) {
        logWarn(e);
        return ResponseEntity.status(Error.UNAUTHORIZED.status())
                .body(new ErrorResponse(Error.UNAUTHORIZED.message()));
    }

    /**
     * Handle access denied exceptions
     *
     * @param e Exception for unauthorized request
     * @return {@link Error} FORBIDDEN
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> accessDeniedExceptionHandler(
            final AccessDeniedException e) {
        logWarn(e);
        return ResponseEntity.status(Error.FORBIDDEN.status())
                .body(new ErrorResponse(Error.FORBIDDEN.message()));
    }

    /**
     * Handle header binding error exceptions
     *
     * @param e {@link RequestHeader} binding exception
     * @return {@link Error} INVALID_ARGUMENT
     */
    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<ErrorResponse> missingRequestHeaderExceptionHandler(
            final MissingRequestHeaderException e) {
        logWarn(e);
        return ResponseEntity.badRequest()
                .body(new ErrorResponse(Error.INVALID_ARGUMENT.message()));
    }

    /**
     * Handle data binding error exceptions
     *
     * @param e Argument binding exception
     * @return HTTP 400 BAD_REQUEST including binding error fields
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<FieldErrorResponse> constraintViolationExceptionHandler(
            final ConstraintViolationException e) {
        logWarn(e);
        return ResponseEntity.badRequest()
                .body(FieldErrorResponse.from(e.getConstraintViolations()));
    }

    /**
     * Handle data binding error exceptions
     *
     * @param e {@link Validated} and {@link Valid} argument binding exception
     * @return HTTP 400 BAD_REQUEST including binding error fields
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<FieldErrorResponse> handleMethodArgumentNotValid(
            final MethodArgumentNotValidException e) {
        logWarn(e);
        return ResponseEntity.badRequest().body(FieldErrorResponse.from(e.getBindingResult()));
    }

    /**
     * Handle all other exceptions
     *
     * @param e Unexpected exception
     * @return {@link Error} INTERNAL_SERVER_ERROR
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(final Exception e) {
        logError(e);
        return ResponseEntity.internalServerError()
                .body(new ErrorResponse(Error.INTERNAL_SERVER_ERROR.message()));
    }

    private void logWarn(final Exception e) {
        log.warn(LOG_FORMAT, e.getClass().getSimpleName(), e.getMessage());
    }

    private void logError(final Exception e) {
        log.error(LOG_FORMAT, e.getClass().getSimpleName(), e.getMessage(), e);
    }
}
