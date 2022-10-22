package me.jaeyeop.blog.user.adapter.in;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;

public final class UserRequest {

  private UserRequest() {
  }

  @Schema
  public record Find(String email) {

  }

  public record Update(@NotBlank String name, String picture) {

  }

  public record Delete(String email) {

  }

}
