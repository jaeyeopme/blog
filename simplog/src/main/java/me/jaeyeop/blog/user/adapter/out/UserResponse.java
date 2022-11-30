package me.jaeyeop.blog.user.adapter.out;

import io.swagger.v3.oas.annotations.media.Schema;
import me.jaeyeop.blog.user.domain.User;

public final class UserResponse {

  private UserResponse() {
  }

  @Schema(name = "User Information Response", title = "사용자 프로필 응답")
  public record Profile(@Schema(description = "이메일") String email,
                        @Schema(description = "이름") String name,
                        @Schema(description = "프로필 사진") String picture,
                        @Schema(description = "소셜 로그인") String provider) {

    public static Profile from(final User user) {
      return new Profile(
          user.email(),
          user.name(),
          user.picture(),
          user.provider().name());
    }

  }

}
