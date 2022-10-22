package me.jaeyeop.blog.post.adapter.in;

import javax.validation.constraints.NotBlank;

public final class PostRequest {

  private PostRequest() {
  }

  public record Create(@NotBlank String title, String content) {

  }

  public record Find(Long postId) {

  }

  public record Update(@NotBlank String title, String content) {

  }

  public record Delete(Long postId) {

  }

}
