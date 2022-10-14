package me.jaeyeop.blog.post.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import me.jaeyeop.blog.config.jpa.AbstractTimeAuditing;
import me.jaeyeop.blog.user.domain.User;

@Entity
@Getter
public class Post extends AbstractTimeAuditing {

  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Id
  private Long id;

  @NotBlank
  @Column(nullable = false)
  private String title;

  @Column
  private String content;

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(nullable = false)
  private User author;

  protected Post() {
  }

  @Builder(access = AccessLevel.PACKAGE)
  private Post(final Long id,
      final String title,
      final String content,
      final User author) {
    this.id = id;
    this.title = title;
    this.content = content;
    this.author = author;
  }

  public static Post of(
      final Long authorId,
      final String title,
      final String content) {
    return Post.builder()
        .title(title)
        .content(content)
        .author(User.proxy(authorId))
        .build();
  }

  public boolean isInaccessible(final Long authorId) {
    return !authorId.equals(this.author.getId());
  }

  public void updateInformation(final String title,
      final String content) {
    this.title = title;
    this.content = content;
  }

}
