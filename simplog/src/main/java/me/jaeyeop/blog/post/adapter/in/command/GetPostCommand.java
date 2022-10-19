package me.jaeyeop.blog.post.adapter.in.command;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class GetPostCommand {

  private final Long postId;

  public GetPostCommand(final Long postId) {
    this.postId = postId;
  }

}
