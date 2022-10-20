package me.jaeyeop.blog.comment.adapter.in;

import javax.validation.constraints.NotBlank;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class UpdateCommentCommand {

  @NotBlank
  private String content;

  UpdateCommentCommand() {
  }

  public UpdateCommentCommand(final String content) {
    this.content = content;
  }

}
