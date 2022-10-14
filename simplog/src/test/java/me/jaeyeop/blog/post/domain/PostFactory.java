package me.jaeyeop.blog.post.domain;

import me.jaeyeop.blog.post.adapter.in.response.PostInformation;
import me.jaeyeop.blog.user.domain.User;

public abstract class PostFactory {

  private static final String DEFAULT_POST_TITLE = "title";

  private static final String DEFAULT_POST_CONTENT = "content";

  private static final String DEFAULT_POST_AUTHOR_NAME = "email@email.com";

  public static Post createPost1() {
    return Post.builder()
        .id(1L)
        .title(DEFAULT_POST_TITLE)
        .content(DEFAULT_POST_CONTENT)
        .build();
  }

  public static Post createPost1(final User author) {
    return Post.builder()
        .id(1L)
        .title(DEFAULT_POST_TITLE)
        .content(DEFAULT_POST_CONTENT)
        .author(author)
        .build();
  }

  public static PostInformation createInformation() {
    return new PostInformation(
        DEFAULT_POST_TITLE,
        DEFAULT_POST_CONTENT,
        null,
        null,
        DEFAULT_POST_AUTHOR_NAME);
  }

}
