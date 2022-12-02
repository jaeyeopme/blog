package me.jaeyeop.blog.support.helper;

import java.util.List;
import java.util.stream.IntStream;
import me.jaeyeop.blog.comment.adapter.out.CommentResponse.Info;
import me.jaeyeop.blog.comment.domain.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

/**
 * @author jaeyeopme Created on 12/01/2022.
 */
public final class CommentHelper {

  private static final String DEFAULT_CONTENT = "content";

  private static final String DEFAULT_AUTHOR_NAME = "author";

  private CommentHelper() {
  }

  public static Comment createWithAuthor(final Long authorId) {
    return Comment.of(DEFAULT_CONTENT, authorId);
  }

  public static Page<Info> createInfoPage(final PageRequest pageable) {
    final List<Info> content = IntStream.range(
            pageable.getPageNumber(),
            pageable.getPageSize())
        .mapToObj(CommentHelper::createInfo)
        .toList();
    return new PageImpl<>(content, pageable, content.size());
  }

  private static Info createInfo(final int id) {
    return new Info(
        (long) id,
        DEFAULT_CONTENT,
        DEFAULT_AUTHOR_NAME,
        null,
        null);
  }

}
