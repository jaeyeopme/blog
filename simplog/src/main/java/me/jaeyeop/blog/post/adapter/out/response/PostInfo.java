package me.jaeyeop.blog.post.adapter.out.response;

import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDateTime;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class PostInfo {

  private final Long id;

  private final String title;

  private final String content;

  private final String author;

  private final LocalDateTime createdAt;

  private final LocalDateTime modifiedAt;

  @QueryProjection
  public PostInfo(final Long id,
      final String title,
      final String content,
      final String author,
      final LocalDateTime createdAt,
      final LocalDateTime modifiedAt) {
    this.id = id;
    this.title = title;
    this.content = content;
    this.author = author;
    this.createdAt = createdAt;
    this.modifiedAt = modifiedAt;
  }

}
