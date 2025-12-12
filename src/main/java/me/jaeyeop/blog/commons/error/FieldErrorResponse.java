package me.jaeyeop.blog.commons.error;

import jakarta.validation.ConstraintViolation;
import java.util.List;
import java.util.Set;
import org.apache.logging.log4j.util.Strings;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

public record FieldErrorResponse(String message,
                                 List<FieldErrorProperty> errors) {

  public static FieldErrorResponse from(final Set<ConstraintViolation<?>> errors) {
    return new FieldErrorResponse(Error.INVALID_ARGUMENT.message(),
        FieldErrorProperty.of(errors));
  }

  public static FieldErrorResponse from(final BindingResult errors) {
    return new FieldErrorResponse(Error.INVALID_ARGUMENT.message(),
        FieldErrorProperty.of(errors));
  }

  private record FieldErrorProperty(String field,
                                    String value,
                                    String message) {

    private static List<FieldErrorProperty> of(final BindingResult errors) {
      final List<FieldError> fieldErrors = errors.getFieldErrors();
      return fieldErrors.stream()
          .map(FieldErrorProperty::map)
          .toList();
    }

    private static FieldErrorProperty map(final FieldError error) {
      final var value = error.getRejectedValue();
      return new FieldErrorProperty(
          error.getField().split("\\.")[0],
          value != null ? value.toString() : Strings.EMPTY,
          error.getDefaultMessage());
    }

    private static List<FieldErrorProperty> of(final Set<ConstraintViolation<?>> errors) {
      return errors.stream()
          .map(FieldErrorProperty::map)
          .filter(FieldErrorProperty::notArg)
          .toList();
    }

    private static boolean notArg(final FieldErrorProperty f) {
      return !f.field().contains("arg");
    }

    private static FieldErrorProperty map(final ConstraintViolation<?> error) {
      final var value = error.getInvalidValue();
      final var fieldName = getField(error.getPropertyPath().toString());
      return new FieldErrorProperty(
          fieldName,
          value != null ? value.toString() : Strings.EMPTY,
          error.getMessage());
    }

    private static String getField(final String propertyPath) {
      return propertyPath.substring(propertyPath.lastIndexOf('.') + 1);
    }

  }

}
