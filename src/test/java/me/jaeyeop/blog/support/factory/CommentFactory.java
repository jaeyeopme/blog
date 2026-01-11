package me.jaeyeop.blog.support.factory;

import java.util.List;
import java.util.stream.IntStream;
import me.jaeyeop.blog.comment.adapter.out.CommentInformationProjectionDto;
import me.jaeyeop.blog.comment.domain.Comment;
import me.jaeyeop.blog.comment.domain.CommentInformation;
import me.jaeyeop.blog.post.domain.Post;
import me.jaeyeop.blog.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

public final class CommentFactory {
    private static final String DEFAULT_CONTENT = "content";
    private static final String DEFAULT_AUTHOR_NAME = "author";

    private CommentFactory() {}

    public static Comment create(final Post post, final User author) {
        return Comment.of(post, author, new CommentInformation(DEFAULT_CONTENT));
    }

    public static Page<CommentInformationProjectionDto> createInformationPage(
            final PageRequest pageable) {
        final List<CommentInformationProjectionDto> content =
                IntStream.range(pageable.getPageNumber(), pageable.getPageSize())
                        .asLongStream()
                        .mapToObj(CommentFactory::createInformation)
                        .toList();
        return new PageImpl<>(content, pageable, content.size());
    }

    public static CommentInformationProjectionDto createInformation(final Long commentId) {
        return new CommentInformationProjectionDto(
                commentId, DEFAULT_CONTENT, DEFAULT_AUTHOR_NAME, null, null);
    }
}
