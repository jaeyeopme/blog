package me.jaeyeop.blog.user.adapter.in.command;

import javax.validation.constraints.NotBlank;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class UpdateUserProfileCommand {

  @NotBlank
  private String name;

  private String picture;

  UpdateUserProfileCommand() {
  }

  public UpdateUserProfileCommand(final String name,
      final String picture) {
    this.name = name;
    this.picture = picture;
  }

}
