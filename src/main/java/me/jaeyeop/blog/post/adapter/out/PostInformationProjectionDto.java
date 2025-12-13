package me.jaeyeop.blog.post.adapter.out;

import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import me.jaeyeop.blog.post.domain.PostInformation;

@Schema(name = "Post Information Response", title = "게시글 정보 응답")
public record PostInformationProjectionDto(
        @Schema(description = "식별자") Long id,
        @Schema(description = "제목") String title,
        @Schema(description = "내용") String content,
        @Schema(description = "커버 이미지") String coverImage,
        @Schema(description = "작성자 이름") String authorName,
        @Schema(description = "작성일") LocalDateTime createdAt,
        @Schema(description = "수정일") LocalDateTime lastModifiedAt) {

    @QueryProjection
    public PostInformationProjectionDto(
            final Long id,
            final PostInformation information,
            final String authorName,
            final LocalDateTime createdAt,
            final LocalDateTime lastModifiedAt) {
        this(
                id,
                information.title(),
                information.content(),
                information.coverImage(),
                authorName,
                createdAt,
                lastModifiedAt);
    }
}
