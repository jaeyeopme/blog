package me.jaeyeop.blog.post.adapter.in.command;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class GetPostInformationCommand {

  private final Long id;

  public GetPostInformationCommand(final Long id) {
    this.id = id;
  }

}
