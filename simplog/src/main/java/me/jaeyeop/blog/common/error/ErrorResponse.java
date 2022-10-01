package me.jaeyeop.blog.common.error;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import java.util.Set;
import javax.validation.ConstraintViolation;
import lombok.Getter;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

  private final String message;

  private List<FieldErrorResponse> errors;

  private ErrorResponse(final String message) {
    this.message = message;
  }

  private ErrorResponse(final String message,
      final Set<ConstraintViolation<?>> errors) {
    this.message = message;
    this.errors = FieldErrorResponse.of(errors);
  }

  private ErrorResponse(final String message,
      final BindingResult errors) {
    this.message = message;
    this.errors = FieldErrorResponse.of(errors);
  }

  public static ResponseEntity<ErrorResponse> of(final ErrorCode code) {
    return ResponseEntity
        .status(code.getStatus())
        .body(new ErrorResponse(code.getMessage()));
  }

  public static ResponseEntity<ErrorResponse> of(final Set<ConstraintViolation<?>> errors) {
    return ResponseEntity
        .status(ErrorCode.INVALID_ARGUMENT.getStatus())
        .body(new ErrorResponse(ErrorCode.INVALID_ARGUMENT.getMessage(), errors));
  }

  public static ResponseEntity<Object> of(final BindingResult errors) {
    return ResponseEntity
        .status(ErrorCode.INVALID_ARGUMENT.getStatus())
        .body(new ErrorResponse(ErrorCode.INVALID_ARGUMENT.getMessage(), errors));
  }

}
