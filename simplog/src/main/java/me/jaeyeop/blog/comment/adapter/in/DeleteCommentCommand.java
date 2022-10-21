package me.jaeyeop.blog.comment.adapter.in;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class DeleteCommentCommand {

  private final Long id;

  public DeleteCommentCommand(final Long id) {
    this.id = id;
  }

}
