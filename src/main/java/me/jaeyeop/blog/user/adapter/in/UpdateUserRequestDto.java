package me.jaeyeop.blog.user.adapter.in;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "Update User Request", title = "사용자 프로필 수정 요청")
public
record UpdateUserRequestDto(
    @Schema(description = "이름") String name,
    @Schema(description = "소개") String introduce
) {

}
