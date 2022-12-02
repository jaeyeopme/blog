package me.jaeyeop.blog.support.helper;

import java.time.LocalDateTime;
import me.jaeyeop.blog.post.adapter.out.PostResponse.Info;
import me.jaeyeop.blog.post.domain.Post;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * @author jaeyeopme Created on 12/01/2022.
 */
public final class PostHelper {

  private static final String DEFAULT_TITLE = "title";

  private static final String DEFAULT_CONTENT = "content";

  private static final Long DEFAULT_AUTHOR_ID = 1L;

  private static final String DEFAULT_AUTHOR_NAME = "author";

  private PostHelper() {
  }

  public static Post create(final Long postId) {
    final var post = Post.of(DEFAULT_TITLE, DEFAULT_CONTENT, DEFAULT_AUTHOR_ID);
    ReflectionTestUtils.setField(post, "id", postId);
    return post;
  }

  public static Post createWithAuthor(final Long authorId) {
    return Post.of(DEFAULT_TITLE, DEFAULT_CONTENT, authorId);
  }

  public static Info createInfo(final Long postId) {
    final var now = LocalDateTime.now();
    return new Info(
        postId,
        DEFAULT_TITLE,
        DEFAULT_CONTENT,
        DEFAULT_AUTHOR_NAME,
        now, now);
  }

}
