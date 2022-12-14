package me.jaeyeop.blog.user.adapter.in;

import io.swagger.v3.oas.annotations.media.Schema;
import me.jaeyeop.blog.user.domain.Profile;

@Schema(name = "User Profile Response", title = "사용자 프로필 응답")
record UserProfileResponseDto(
    @Schema(description = "이메일") String email,
    @Schema(description = "이름") String name,
    @Schema(description = "프로필 사진") String picture
) {

  public static UserProfileResponseDto from(final Profile profile) {
    return new UserProfileResponseDto(
        profile.email(),
        profile.name(),
        profile.picture());
  }

}
