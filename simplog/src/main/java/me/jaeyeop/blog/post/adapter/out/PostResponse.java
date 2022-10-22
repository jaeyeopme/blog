package me.jaeyeop.blog.post.adapter.out;

import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDateTime;

public final class PostResponse {

  private PostResponse() {
  }

  public record Info(Long id,
                     String title,
                     String content,
                     String authorName,
                     LocalDateTime createdAt,
                     LocalDateTime modifiedAt) {

    @QueryProjection
    public Info {
    }

  }

}
