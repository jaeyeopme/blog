package me.jaeyeop.blog.comment.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import me.jaeyeop.blog.config.error.exception.PrincipalAccessDeniedException;
import me.jaeyeop.blog.config.jpa.AbstractBaseEntity;
import me.jaeyeop.blog.post.domain.Post;
import me.jaeyeop.blog.user.domain.User;

/**
 * @author jaeyeopme Created on 10/16/2022.
 */
@Entity
@Getter
public class Comment extends AbstractBaseEntity {

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

  private Comment(
      final String content,
      final User author) {
    this.content = content;
    this.author = author;
  }

  public static Comment of(
      final String content,
      final Long authorId) {
    return new Comment(content, User.reference(authorId));
  }

  public void post(final Post post) {
    this.post = post;
  }

  public void confirmAccess(final Long authorId) {
    if (!this.author.id().equals(authorId)) {
      throw new PrincipalAccessDeniedException();
    }
  }

  public void updateInformation(final String content) {
    this.content = content;
  }

}
