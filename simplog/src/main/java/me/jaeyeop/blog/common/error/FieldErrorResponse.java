package me.jaeyeop.blog.common.error;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.ConstraintViolation;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class FieldErrorResponse {

  private final String field;

  private final String value;

  private final String reason;

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
