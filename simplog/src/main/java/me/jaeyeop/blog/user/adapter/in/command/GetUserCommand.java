package me.jaeyeop.blog.user.adapter.in.command;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class GetUserCommand {

  private final String email;

  public GetUserCommand(final String email) {
    this.email = email;
  }

}
