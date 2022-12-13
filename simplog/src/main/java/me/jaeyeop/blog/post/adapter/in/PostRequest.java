package me.jaeyeop.blog.post.adapter.in;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;

/**
 * @author jaeyeopme Created on 10/22/2022.
 */
public final class PostRequest {

  private PostRequest() {
  }

  @Schema(name = "Create Post Request", title = "게시글 작성 요청")
  public record Create(@Schema(description = "제목")
                       @NotBlank String title,
                       @Schema(description = "내용")
                       @NotBlank String content) {

  }

  public record Find(Long postId) {

  }

  @Schema(name = "Update Post Request", title = "게시글 수정 요청")
  public record Update(@Schema(description = "제목")
                       String title,
                       @Schema(description = "내용")
                       String content) {

  }

  public record Delete(Long postId) {

  }

}
