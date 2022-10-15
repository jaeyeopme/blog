package me.jaeyeop.blog.post.adapter.in.command;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class DeletePostInformationCommand {

  private final Long id;

  public DeletePostInformationCommand(final Long id) {
    this.id = id;
  }

}
