package me.jaeyeop.blog.post.domain;

import static me.jaeyeop.blog.post.adapter.out.PostResponse.Info;
import me.jaeyeop.blog.user.domain.User;

public abstract class PostFactory {

  private static final String DEFAULT_TITLE = "title";

  private static final String DEFAULT_CONTENT = "content";

  private static final String DEFAULT_AUTHOR_NAME = "author";

  public static Post createPost(final Long postId) {
    return Post.builder()
        .id(postId)
        .title(DEFAULT_TITLE)
        .content(DEFAULT_CONTENT)
        .build();
  }

  public static Post createPost1(final User author) {
    return Post.builder()
        .id(1L)
        .title(DEFAULT_TITLE)
        .content(DEFAULT_CONTENT)
        .author(author)
        .build();
  }

  public static Info createInfo(final Long postId) {
    return new Info(
        postId,
        DEFAULT_TITLE,
        DEFAULT_CONTENT,
        DEFAULT_AUTHOR_NAME,
        null,
        null);
  }

}
