package me.jaeyeop.blog.user.adapter.in.command;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class DeleteUserCommand {

  private final String email;

  public DeleteUserCommand(final String email) {
    this.email = email;
  }

}
