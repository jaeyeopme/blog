package me.jaeyeop.blog.post.adapter.in;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import me.jaeyeop.blog.commons.config.oas.OASResponse.InvalidArgumentResponse;
import me.jaeyeop.blog.commons.config.oas.OASResponse.NotFoundPostResponse;
import me.jaeyeop.blog.commons.config.oas.OASResponse.SecurityResponse;
import me.jaeyeop.blog.commons.config.security.UserPrincipal;
import me.jaeyeop.blog.post.adapter.out.PostInformationProjectionDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;

@Validated
@Tag(
    name = "3.post",
    description = "게시글(게시글 작성, 게시글 조회, 게시글 수정, 게시글 삭제)"
)
public interface PostOAS {

  @InvalidArgumentResponse
  @SecurityResponse
  @ApiResponse(
      responseCode = "201",
      description = "게시글 작성 성공",
      headers = @Header(name = HttpHeaders.LOCATION, description = "게시글 조회 URI", required = true)
  )
  @Operation(
      summary = "Write my post",
      description = "게시글을 작성합니다."
  )
  ResponseEntity<Void> write(
      UserPrincipal principal,
      @Validated WritePostRequestDto request
  );

  @NotFoundPostResponse
  @ApiResponse(
      responseCode = "200",
      description = "게시글 조회 성공",
      content = @Content(schema = @Schema(implementation = PostInformationProjectionDto.class))
  )
  @Operation(
      summary = "Find post by post id",
      description = "게시글을 조회합니다."
  )
  PostInformationProjectionDto findInformationById(@Schema(description = "게시글 식별자") Long postId);

  @InvalidArgumentResponse
  @NotFoundPostResponse
  @SecurityResponse
  @ApiResponse(
      responseCode = "204",
      description = "자신의 게시글 수정 성공"
  )
  @Operation(
      summary = "Edit my post by post id",
      description = "자신의 게시글을 수정합니다."
  )
  void edit(
      UserPrincipal principal,
      @Schema(description = "자신의 게시글 식별자") Long postId,
      EditPostRequestDto request
  );

  @NotFoundPostResponse
  @SecurityResponse
  @ApiResponse(
      responseCode = "204",
      description = "자신의 게시글 삭제 성공"
  )
  @Operation(
      summary = "Delete my post by post id",
      description = "자신의 게시글을 삭제합니다."
  )
  void delete(
      UserPrincipal principal,
      @Schema(description = "자신의 게시글 식별자") Long postId
  );

}
