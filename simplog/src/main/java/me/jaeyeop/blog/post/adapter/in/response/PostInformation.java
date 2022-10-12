package me.jaeyeop.blog.post.adapter.in.response;

import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDateTime;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class PostInformation {

  private final String title;

  private final String content;

  private final LocalDateTime createdAt;

  private final LocalDateTime modifiedAt;

  private final String author;

  @QueryProjection
  public PostInformation(final String title,
      final String content,
      final LocalDateTime createdAt,
      final LocalDateTime modifiedAt,
      final String author) {
    this.title = title;
    this.content = content;
    this.createdAt = createdAt;
    this.modifiedAt = modifiedAt;
    this.author = author;
  }

}
