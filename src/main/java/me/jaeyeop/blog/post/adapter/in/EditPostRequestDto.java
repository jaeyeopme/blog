package me.jaeyeop.blog.post.adapter.in;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "Edit Post Request", title = "게시글 수정 요청")
public record EditPostRequestDto(
        @Schema(description = "제목") String title, @Schema(description = "내용") String content) {}
