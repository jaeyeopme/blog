package me.jaeyeop.blog.post.adapter.in.command;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class DeletePostCommand {

  private final Long id;

  public DeletePostCommand(final Long id) {
    this.id = id;
  }

}
