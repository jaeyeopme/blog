package me.jaeyeop.blog.config.openapi.spec;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import me.jaeyeop.blog.config.openapi.SecurityOpenApiResponses;
import org.springframework.http.MediaType;

@SecurityOpenApiResponses
@Tag(name = "1. auth", description = "인증(토큰 만료, 엑세스 토큰 재발급)")
public interface AuthOpenApiSpec {

  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "204",
          description = "토큰 만료 성공"),
  })
  @Operation(summary = "Expire access token and refresh token",
      description = "엑세스 토큰과 리프레시 토큰을 만료합니다.")
  void expire(
      @Schema(hidden = true)
      String accessToken,
      @Schema(description = "리프레시 토큰",
          example = "Bearer ")
      String refreshToken);

  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "201",
          description = "엑세스 토큰 재발급 성공",
          content = @Content(schema = @Schema(description = "access token"), mediaType = MediaType.TEXT_PLAIN_VALUE)),
  })
  @Operation(summary = "Refresh access token by refresh token",
      description = "엑세스 토큰을 재발급합니다.")
  String refresh(
      @Schema(description = "리프레시 토큰",
          example = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE2 ...")
      String refreshToken);

}
