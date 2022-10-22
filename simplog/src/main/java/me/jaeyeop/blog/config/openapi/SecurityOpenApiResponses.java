package me.jaeyeop.blog.config.openapi;

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
import me.jaeyeop.blog.config.error.ErrorResponse;

@ApiResponses(value = {
    @ApiResponse(
        responseCode = "401",
        description = "인증 실패(유효하지 않은 엑세스 토큰)",
        content = @Content(schema = @Schema(implementation = ErrorResponse.class))),

    @ApiResponse(
        responseCode = "403",
        description = "인가 실패(요청 권한 없음)",
        content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
})
@SecurityRequirement(name = "access_token")
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@Documented
public @interface SecurityOpenApiResponses {

}
