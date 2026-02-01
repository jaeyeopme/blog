package me.jaeyeop.blog.comment.adapter.in;

import jakarta.validation.constraints.NotBlank;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "Edit Comment Request", title = "댓글 수정 요청")
public record EditCommentRequestDto(@Schema(description = "내용") @NotBlank String content) {}
