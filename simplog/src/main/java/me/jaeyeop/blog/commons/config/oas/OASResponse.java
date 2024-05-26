package me.jaeyeop.blog.commons.config.oas;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import me.jaeyeop.blog.commons.error.ErrorResponse;

public final class OASResponse {

  private OASResponse() {
  }

  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "401",
          description = "유효하지 않은 토큰인 경우(인증 실패)",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class))
      ),

      @ApiResponse(
          responseCode = "403",
          description = "접근 권한이 없는 경우(인가 실패)",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class))
      ),
  })
  @SecurityRequirement(name = "access_token")
  @Retention(RetentionPolicy.RUNTIME)
  @Target({ElementType.METHOD, ElementType.TYPE})
  @Documented
  public @interface SecurityResponse {

  }

  @ApiResponse(
      responseCode = "400",
      description = "잘못된 입력 값인 경우",
      content = @Content(schema = @Schema(implementation = ErrorResponse.class))
  )
  @Retention(RetentionPolicy.RUNTIME)
  @Target({ElementType.METHOD, ElementType.TYPE})
  @Documented
  public @interface InvalidArgumentResponse {

  }

  @ApiResponse(
      responseCode = "404",
      description = "존재하지 않은 사용자인 경우",
      content = @Content(schema = @Schema(implementation = ErrorResponse.class))
  )
  @Retention(RetentionPolicy.RUNTIME)
  @Target({ElementType.METHOD, ElementType.TYPE})
  @Documented
  public @interface NotFoundUserResponse {

  }

  @ApiResponse(
      responseCode = "404",
      description = "존재하지 않은 게시글인 경우",
      content = @Content(schema = @Schema(implementation = ErrorResponse.class))
  )
  @Retention(RetentionPolicy.RUNTIME)
  @Target({ElementType.METHOD, ElementType.TYPE})
  @Documented
  public @interface NotFoundPostResponse {

  }

  @ApiResponse(
      responseCode = "404",
      description = "존재하지 않은 댓글인 경우",
      content = @Content(schema = @Schema(implementation = ErrorResponse.class))
  )
  @Retention(RetentionPolicy.RUNTIME)
  @Target({ElementType.METHOD, ElementType.TYPE})
  @Documented
  public @interface NotFoundCommentResponse {

  }

}
