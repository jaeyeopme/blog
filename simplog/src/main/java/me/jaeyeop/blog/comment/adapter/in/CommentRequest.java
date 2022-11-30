package me.jaeyeop.blog.comment.adapter.in;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import org.springframework.data.domain.Pageable;

public final class CommentRequest {

  private CommentRequest() {
  }

  @Schema(name = "Create Comment Request", title = "댓글 작성 요청")
  public record Create(@Schema(description = "게시글 식별자")
                       @NotNull Long postId,
                       @Schema(description = "내용")
                       @NotBlank String content) {

  }

  public record Find(Long postId, Pageable pageable) {

  }

  @Schema(name = "Update Comment Request", title = "댓글 수정 요청")
  public record Update(@Schema(description = "내용")
                       @NotBlank String content) {

  }

  public record Delete(Long commentId) {

  }

}
