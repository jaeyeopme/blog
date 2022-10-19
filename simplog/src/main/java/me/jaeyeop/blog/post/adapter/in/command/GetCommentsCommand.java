package me.jaeyeop.blog.post.adapter.in.command;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.data.domain.Pageable;

@Getter
@EqualsAndHashCode
public class GetCommentsCommand {

  private final Long postId;

  private final Pageable commentsPageable;

  public GetCommentsCommand(final Long postId,
      final Pageable commentsPageable) {
    this.postId = postId;
    this.commentsPageable = commentsPageable;
  }

}
