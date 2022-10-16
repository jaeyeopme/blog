package me.jaeyeop.blog.comment.domain;

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
import me.jaeyeop.blog.post.domain.Post;
import me.jaeyeop.blog.user.domain.User;

@Getter
@Entity
public class Comment extends AbstractTimeAuditing {

  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Id
  private Long id;

  @NotBlank
  @Column(nullable = false)
  private String content;

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(nullable = false, updatable = false)
  private User author;

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(nullable = false, updatable = false)
  private Post post;

  protected Comment() {
  }

  @Builder(access = AccessLevel.PACKAGE)
  public Comment(final Long id,
      final String content,
      final User author,
      final Post post) {
    this.id = id;
    this.content = content;
    this.author = author;
    this.post = post;
  }

}
