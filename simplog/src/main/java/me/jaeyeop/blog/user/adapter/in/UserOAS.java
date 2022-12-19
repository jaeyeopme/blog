package me.jaeyeop.blog.user.adapter.in;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.ConstraintDeclarationException;
import javax.validation.constraints.Email;
import me.jaeyeop.blog.commons.config.oas.OASResponse.InvalidArgumentResponse;
import me.jaeyeop.blog.commons.config.oas.OASResponse.NotFoundUserResponse;
import me.jaeyeop.blog.commons.config.oas.OASResponse.SecurityResponse;
import me.jaeyeop.blog.commons.config.security.UserPrincipal;
import org.springframework.validation.annotation.Validated;

/**
 * {@link Validated} 어노테이션을 사용할 때 LSP 위배를 주의해야 한다.
 *
 * @see ConstraintDeclarationException
 */
@Validated
@Tag(
    name = "2.user",
    description = "사용자(프로필 조회, 프로필 수정, 프로필 삭제)"
)
public interface UserOAS {

  @SecurityResponse
  @ApiResponse(
      responseCode = "200",
      description = "자신의 프로필 조회 성공",
      content = @Content(schema = @Schema(implementation = UserProfileResponseDto.class))
  )
  @Operation(
      summary = "Find my profile",
      description = "자신의 프로필을 조회합니다."
  )
  UserProfileResponseDto findById(UserPrincipal principal);

  @InvalidArgumentResponse
  @NotFoundUserResponse
  @ApiResponse(
      responseCode = "200",
      description = "사용자 프로필 조회 성공",
      content = @Content(schema = @Schema(implementation = UserProfileResponseDto.class))
  )
  @Operation(
      summary = "Find user profile by user email",
      description = "사용자의 프로필을 조회합니다."
  )
  UserProfileResponseDto findByEmail(@Schema(description = "사용자 이메일") @Email String email);

  @InvalidArgumentResponse
  @SecurityResponse
  @ApiResponse(
      responseCode = "204",
      description = "자신의 프로필 업데이트 성공"
  )
  @Operation(
      summary = "Update my profile",
      description = "자신의 프로필을 수정합니다."
  )
  void update(
      UserPrincipal principal,
      UpdateUserRequestDto request
  );

  @SecurityResponse
  @ApiResponse(
      responseCode = "204",
      description = "자신의 프로필 삭제 성공"
  )
  @Operation(
      summary = "Delete my profile",
      description = "자신의 프로필을 삭제합니다.(작성한 게시글과 댓글 모두 삭제)"
  )
  void delete(UserPrincipal principal);

}
