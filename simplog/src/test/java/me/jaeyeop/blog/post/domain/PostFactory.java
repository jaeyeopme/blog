package me.jaeyeop.blog.post.domain;

import me.jaeyeop.blog.post.adapter.out.response.PostInfo;
import me.jaeyeop.blog.user.domain.User;

public abstract class PostFactory {

  private static final String DEFAULT_TITLE = "title";

  private static final String DEFAULT_CONTENT = "content";

  private static final String DEFAULT_AUTHOR_NAME = "author";

  public static Post createPost1() {
    return Post.builder()
        .id(1L)
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

  public static PostInfo createInfo(final Long postId) {
    return new PostInfo(
        postId,
        DEFAULT_TITLE,
        DEFAULT_CONTENT,
        DEFAULT_AUTHOR_NAME,
        null,
        null);
  }

}
