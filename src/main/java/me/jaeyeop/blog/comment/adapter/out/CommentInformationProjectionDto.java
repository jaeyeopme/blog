package me.jaeyeop.blog.comment.adapter.out;

import java.time.LocalDateTime;

import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;

import me.jaeyeop.blog.comment.domain.CommentInformation;

@Schema(name = "Comment Information Response", title = "댓글 정보 응답")
public record CommentInformationProjectionDto(
        @Schema(description = "식별자") Long id,
        @Schema(description = "작성자") String authorName,
        @Schema(description = "내용") String content,
        @Schema(description = "작성일") LocalDateTime createdAt,
        @Schema(description = "수정일") LocalDateTime modifiedAt) {
    @QueryProjection
    public CommentInformationProjectionDto(
            final Long id,
            final String authorName,
            final CommentInformation information,
            final LocalDateTime createdAt,
            final LocalDateTime modifiedAt) {
        this(id, information.content(), authorName, createdAt, modifiedAt);
    }
}
