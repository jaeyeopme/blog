package me.jaeyeop.blog.user.adapter.in;

import io.swagger.v3.oas.annotations.media.Schema;

public final class UserRequest {

  private UserRequest() {
  }

  public record Find(String email) {

  }

  @Schema(name = "User Update Request", title = "사용자 프로필 수정 요청")
  public record Update(@Schema(description = "이름")
                       String name,
                       @Schema(description = "프로필 사진")
                       String picture) {

  }

  public record Delete(String email) {

  }

}
