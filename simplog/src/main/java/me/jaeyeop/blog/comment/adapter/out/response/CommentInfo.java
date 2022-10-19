package me.jaeyeop.blog.comment.adapter.out.response;

import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDateTime;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class CommentInfo {

  private final Long id;

  private final String content;

  private final String author;

  private final LocalDateTime createdAt;

  private final LocalDateTime modifiedAt;

  @QueryProjection
  public CommentInfo(final Long id,
      final String content,
      final String author,
      final LocalDateTime createdAt,
      final LocalDateTime modifiedAt) {
    this.id = id;
    this.content = content;
    this.author = author;
    this.createdAt = createdAt;
    this.modifiedAt = modifiedAt;
  }

}
