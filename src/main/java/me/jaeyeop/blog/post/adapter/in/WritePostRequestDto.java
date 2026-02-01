package me.jaeyeop.blog.post.adapter.in;

import jakarta.validation.constraints.NotBlank;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "Write Post Request", title = "게시글 작성 요청")
public record WritePostRequestDto(
        @Schema(description = "제목") @NotBlank String title,
        @Schema(description = "내용") @NotBlank String content) {}
