package me.jaeyeop.blog.support.helper;

import java.time.LocalDateTime;
import me.jaeyeop.blog.post.adapter.out.PostResponse.Info;
import me.jaeyeop.blog.post.domain.Post;
import me.jaeyeop.blog.user.domain.User;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * @author jaeyeopme Created on 12/01/2022.
 */
public final class PostHelper {

  private static final String DEFAULT_TITLE = "post default title";

  private static final String DEFAULT_CONTENT = "post default content";

  private static final String DEFAULT_AUTHOR_NAME = "post default author";

  private PostHelper() {
  }

  public static Post create(final User author) {
    final var post = Post.of(DEFAULT_TITLE, DEFAULT_CONTENT, author);
    ReflectionTestUtils.setField(post, "id", 1L);

    return post;
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
