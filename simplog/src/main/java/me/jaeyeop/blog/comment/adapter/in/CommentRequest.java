package me.jaeyeop.blog.comment.adapter.in;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import org.springframework.data.domain.Pageable;

public final class CommentRequest {

  private CommentRequest() {
  }

  public record Create(@NotNull Long postId, @NotBlank String content) {

  }

  public record Find(Long postId, Pageable commentPageable) {

  }

  public record Update(@NotBlank String content) {

  }

  public record Delete(Long commentId) {

  }

}
