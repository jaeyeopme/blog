package me.jaeyeop.blog.comment.adapter.in;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import me.jaeyeop.blog.comment.adapter.out.CommentInformationProjectionDto;
import me.jaeyeop.blog.config.oas.dto.OASRequest.PageParameters;
import me.jaeyeop.blog.config.oas.dto.OASResponse.InvalidArgumentResponse;
import me.jaeyeop.blog.config.oas.dto.OASResponse.NotFoundCommentResponse;
import me.jaeyeop.blog.config.oas.dto.OASResponse.SecurityResponse;
import me.jaeyeop.blog.config.security.authentication.UserPrincipal;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;

@Validated
@Tag(
    name = "4. comment",
    description = "댓글(댓글 작성, 댓글 조회, 댓글 수정, 댓글 삭제)"
)
public interface CommentOAS {

  @InvalidArgumentResponse
  @SecurityResponse
  @ApiResponse(
      responseCode = "201",
      description = "댓글 작성 성공",
      headers = @Header(name = HttpHeaders.LOCATION, description = "댓글 조회 URI", required = true)
  )
  @Operation(
      summary = "Write my comment by post id",
      description = "댓글을 작성합니다."
  )
  ResponseEntity<Void> write(
      UserPrincipal principal,
      @Schema(description = "게시글 식별자") @NotNull Long postId,
      @Validated WriteCommentRequestDto request
  );

  @InvalidArgumentResponse
  @NotFoundCommentResponse
  @ApiResponse(
      responseCode = "200",
      description = "댓글 페이지 조회 성공"
  )
  @PageParameters
  @Operation(
      summary = "Find comments by post id",
      description = "댓글들을 조회합니다."
  )
  Page<CommentInformationProjectionDto> findInformationPageByPostId(
      @Schema(description = "게시글 식별자") Long postId,
      @Min(0) @Max(99) int page,
      @Min(0) @Max(99) int size
  );

  @NotFoundCommentResponse
  @ApiResponse(
      responseCode = "200",
      description = "댓글 조회 성공",
      content = @Content(schema = @Schema(implementation = CommentInformationProjectionDto.class))
  )
  @Operation(
      summary = "Find comment by comment id",
      description = "댓글을 조회합니다."
  )
  CommentInformationProjectionDto findInformationById(
      @Schema(description = "댓글 식별자") @PathVariable Long commentId);

  @InvalidArgumentResponse
  @NotFoundCommentResponse
  @SecurityResponse
  @ApiResponse(
      responseCode = "204",
      description = "자신의 댓글 수정 성공"
  )
  @Operation(
      summary = "Edit my comment by comment id",
      description = "자신의 댓글을 수정합니다."
  )
  void edit(
      UserPrincipal principal,
      @Schema(description = "댓글 식별자") Long commentId,
      @Validated EditCommentRequestDto request
  );

  @NotFoundCommentResponse
  @SecurityResponse
  @ApiResponse(
      responseCode = "204",
      description = "자신의 댓글 삭제 성공"
  )
  @Operation(
      summary = "Delete my comment by comment id",
      description = "자신의 댓글을 삭제합니다."
  )
  void delete(
      UserPrincipal principal,
      @Schema(description = "댓글 식별자") Long commentId
  );

}
