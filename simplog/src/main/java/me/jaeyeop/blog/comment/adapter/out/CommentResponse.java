package me.jaeyeop.blog.comment.adapter.out;

import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDateTime;

public final class CommentResponse {

  private CommentResponse() {
  }

  public record Info(Long id,
                     String content,
                     String authorName,
                     LocalDateTime createdAt,
                     LocalDateTime modifiedAt) {

    @QueryProjection
    public Info {
    }

  }

}
