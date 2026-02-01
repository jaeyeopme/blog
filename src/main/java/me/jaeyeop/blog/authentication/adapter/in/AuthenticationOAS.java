package me.jaeyeop.blog.authentication.adapter.in;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import me.jaeyeop.blog.commons.authentication.OAuth2Response;
import me.jaeyeop.blog.commons.config.oas.OASResponse.SecurityResponse;
import org.springframework.http.MediaType;

@Tag(name = "1.authentication", description = "인증(로그아웃, 엑세스 토큰 재발급)")
public interface AuthenticationOAS {
    @SecurityResponse
    @ApiResponse(responseCode = "204", description = "로그아웃 성공")
    @Operation(
            summary = "Expire access token and refresh token",
            description = "엑세스 토큰과 리프레시 토큰을 만료합니다.")
    void logout(
            @Schema(hidden = true) String accessToken,
            @Schema(description = "리프레시 토큰", example = "Bearer ") String refreshToken);

    @SecurityResponse
    @ApiResponse(
            responseCode = "201",
            description = "엑세스 토큰 재발급 성공",
            content =
                    @Content(
                            schema = @Schema(implementation = OAuth2Response.class),
                            mediaType = MediaType.APPLICATION_JSON_VALUE))
    @Operation(summary = "Refresh access token by refresh token", description = "엑세스 토큰을 재발급합니다.")
    OAuth2Response refresh(
            @Schema(description = "리프레시 토큰", example = "Bearer ") String refreshToken);
}
