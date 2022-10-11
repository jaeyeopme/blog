package me.jaeyeop.blog.post.adapter.in.command;

import javax.validation.constraints.NotBlank;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class CreatePostCommand {

  @NotBlank
  private String title;

  private String content;

  private CreatePostCommand() {
  }

  public CreatePostCommand(final String title,
      final String content) {
    this.title = title;
    this.content = content;
  }

}
