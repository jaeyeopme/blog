package me.jaeyeop.blog.user.adapter.in.command;

import javax.validation.constraints.NotBlank;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class UpdateUserCommand {

  @NotBlank
  private String name;

  private String picture;

  UpdateUserCommand() {
  }

  public UpdateUserCommand(final String name,
      final String picture) {
    this.name = name;
    this.picture = picture;
  }

}
