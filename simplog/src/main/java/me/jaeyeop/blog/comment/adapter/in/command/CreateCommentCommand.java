package me.jaeyeop.blog.comment.adapter.in.command;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class CreateCommentCommand {

  @NotNull
  private Long postId;

  @NotBlank
  private String content;

  private CreateCommentCommand() {
  }

  public CreateCommentCommand(final Long postId, final String content) {
    this.postId = postId;
    this.content = content;
  }

}
