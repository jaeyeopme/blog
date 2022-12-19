package me.jaeyeop.blog.commons.config.oas;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public final class OASRequest {

  private OASRequest() {
  }

  @Parameter(
      name = "page",
      in = ParameterIn.QUERY,
      description = "0 ~ 99 범위를 벗어나면 기본 값이 적용됩니다.",
      schema = @Schema(type = "integer($int32)", defaultValue = "0"))
  @Parameter(
      name = "size",
      in = ParameterIn.QUERY,
      description = "0 ~ 99 범위를 벗어나면 기본 값이 적용됩니다.",
      schema = @Schema(type = "integer($int32)", defaultValue = "10"))
  @Parameter(name = "pageable", hidden = true)
  @Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
  @Retention(RetentionPolicy.RUNTIME)
  public @interface PageParameters {

  }

}
