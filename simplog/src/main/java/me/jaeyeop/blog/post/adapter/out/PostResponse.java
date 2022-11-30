package me.jaeyeop.blog.post.adapter.out;

import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

public final class PostResponse {

  private PostResponse() {
  }

  @Schema(name = "Post Information Response", title = "게시글 정보 응답")
  public record Info(@Schema(description = "식별자") Long id,
                     @Schema(description = "제목") String title,
                     @Schema(description = "내용") String content,
                     @Schema(description = "작성자") String authorName,
                     @Schema(description = "작성일") LocalDateTime createdAt,
                     @Schema(description = "수정일") LocalDateTime modifiedAt) {

    @QueryProjection
    public Info {
    }

  }

}
