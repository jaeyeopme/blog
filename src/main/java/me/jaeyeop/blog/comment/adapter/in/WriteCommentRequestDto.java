package me.jaeyeop.blog.comment.adapter.in;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(name = "Write Comment Request", title = "댓글 작성 요청")
public record WriteCommentRequestDto(@Schema(description = "내용") @NotBlank String content) {}
