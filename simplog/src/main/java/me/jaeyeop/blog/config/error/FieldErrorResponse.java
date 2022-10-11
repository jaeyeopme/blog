package me.jaeyeop.blog.config.error;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.ConstraintViolation;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.apache.logging.log4j.util.Strings;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

@Getter
@EqualsAndHashCode
public class FieldErrorResponse {

  private final String field;

  private final String value;

  private final String reason;

  private FieldErrorResponse(final String field,
      final String value,
      final String reason) {
    this.field = field;
    this.value = value;
    this.reason = reason;
  }

  public static List<FieldErrorResponse> of(final BindingResult errors) {
    final List<FieldError> fieldErrors = errors.getFieldErrors();
    return fieldErrors.stream()
        .map(FieldErrorResponse::map)
        .collect(Collectors.toUnmodifiableList());
  }

  public static List<FieldErrorResponse> of(final Set<ConstraintViolation<?>> errors) {
    return errors.stream()
        .map(FieldErrorResponse::map)
        .collect(Collectors.toUnmodifiableList());
  }

  private static FieldErrorResponse map(final FieldError error) {
    final var value = error.getRejectedValue();
    return new FieldErrorResponse(
        error.getField().split("\\.")[0],
        value != null ? value.toString() : Strings.EMPTY,
        error.getDefaultMessage());
  }

  private static FieldErrorResponse map(final ConstraintViolation<?> error) {
    final var value = error.getInvalidValue();
    return new FieldErrorResponse(
        error.getPropertyPath().toString(),
        value != null ? value.toString() : Strings.EMPTY,
        error.getMessage());
  }

}
