package me.jaeyeop.blog.post.domain;

import static me.jaeyeop.blog.user.domain.UserFactory.DEFAULT_USER_EMAIL;
import me.jaeyeop.blog.post.adapter.in.response.PostInformation;
import me.jaeyeop.blog.user.domain.User;

public abstract class PostFactory {

  private static final Long DEFAULT_POST_ID = 1L;

  private static final String DEFAULT_POST_TITLE = "title";

  private static final String DEFAULT_POST_CONTENT = "content";

  public static Post createDefault() {
    return Post.builder()
        .id(DEFAULT_POST_ID)
        .title(DEFAULT_POST_TITLE)
        .content(DEFAULT_POST_CONTENT)
        .build();
  }

  public static Post createDefault(final User defaultAuthor) {
    return Post.builder()
        .id(DEFAULT_POST_ID)
        .title(DEFAULT_POST_TITLE)
        .content(DEFAULT_POST_CONTENT)
        .author(defaultAuthor)
        .build();
  }

  public static PostInformation createInformation() {
    return new PostInformation(
        DEFAULT_POST_TITLE,
        DEFAULT_POST_CONTENT,
        null,
        null,
        DEFAULT_USER_EMAIL);
  }

}
