package me.jaeyeop.blog.user.adapter.in.command;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class GetUserProfileCommand {

  private final String email;

  public GetUserProfileCommand(final String email) {
    this.email = email;
  }

}
