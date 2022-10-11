package me.jaeyeop.blog.user.adapter.in.command;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class DeleteUserProfileCommand {

  private final String email;

  public DeleteUserProfileCommand(final String email) {
    this.email = email;
  }

}
