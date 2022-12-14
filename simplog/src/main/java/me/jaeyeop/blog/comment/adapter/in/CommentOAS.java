package me.jaeyeop.blog.comment.adapter.in;

import static me.jaeyeop.blog.comment.adapter.in.CommentRequest.Create;
import static me.jaeyeop.blog.comment.adapter.in.CommentRequest.Update;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import me.jaeyeop.blog.comment.adapter.out.CommentResponse.Info;
import me.jaeyeop.blog.config.oas.dto.OASRequest.PageParameters;
import me.jaeyeop.blog.config.oas.dto.OASResponse.InvalidArgumentResponse;
import me.jaeyeop.blog.config.oas.dto.OASResponse.NotFoundCommentResponse;
import me.jaeyeop.blog.config.oas.dto.OASResponse.SecurityResponse;
import me.jaeyeop.blog.config.security.authentication.UserPrincipal;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;

@Validated
@Tag(name = "4. comment", description = "댓글(댓글 작성, 댓글 조회, 댓글 수정, 댓글 삭제)")
public interface CommentOAS {

  @NotFoundCommentResponse
  @SecurityResponse
  @Operation(summary = "Delete my comment by comment targetId", description = "자신의 댓글을 삭제합니다.")
  void delete(
      UserPrincipal principal,
      @Schema(description = "댓글 식별자") Long commentId);

  @InvalidArgumentResponse
  @NotFoundCommentResponse
  @PageParameters
  @Operation(summary = "Find comments by post targetId", description = "댓글들을 조회합니다.")
  Page<Info> findPage(
      @Schema(description = "게시글 식별자") Long postId,
      @Min(0) @Max(99) int page,
      @Min(0) @Max(99) int size);

  @InvalidArgumentResponse
  @NotFoundCommentResponse
  @SecurityResponse
  @Operation(summary = "Update my comment by comment targetId", description = "자신의 댓글을 수정합니다.")
  void update(
      UserPrincipal principal,
      @Schema(description = "댓글 식별자") Long commentId,
      @Validated Update request);

  @InvalidArgumentResponse
  @SecurityResponse
  @Operation(summary = "Create comment by post targetId", description = "댓글을 작성합니다.")
  void create(
      UserPrincipal principal,
      @Validated Create request);

}
