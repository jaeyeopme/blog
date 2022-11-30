package me.jaeyeop.blog.comment.adapter.out;

import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

public final class CommentResponse {

  private CommentResponse() {
  }

  @Schema(name = "Comment Information Response", title = "댓글 정보 응답")
  public record Info(@Schema(description = "식별자") Long id,
                     @Schema(description = "내용") String content,
                     @Schema(description = "작성자") String authorName,
                     @Schema(description = "작성일") LocalDateTime createdAt,
                     @Schema(description = "수정일") LocalDateTime modifiedAt) {

    @QueryProjection
    public Info {
    }

  }

}
