package me.jaeyeop.blog.post.domain;

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

}
