package me.jaeyeop.blog.post.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import me.jaeyeop.blog.comment.domain.Comment;
import me.jaeyeop.blog.config.error.exception.PrincipalAccessDeniedException;
import me.jaeyeop.blog.config.jpa.AbstractBaseEntity;
import me.jaeyeop.blog.user.domain.User;
import org.springframework.util.StringUtils;

/**
 * @author jaeyeopme Created on 10/10/2022.
 */
@Entity
@Getter
public class Post extends AbstractBaseEntity {

  @NotBlank
  @Column(nullable = false)
  private String title;

  @Column
  private String content;

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(nullable = false, updatable = false)
  private User author;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "post", orphanRemoval = true)
  private List<Comment> comments = new ArrayList<>();

  protected Post() {
  }

  private Post(
      final String title,
      final String content,
      final User author) {
    this.title = title;
    this.content = content;
    this.author = author;
  }

  public static Post of(
      final String title,
      final String content,
      final Long authorId) {
    return new Post(title, content, User.reference(authorId));
  }

  public void updateInformation(final String title, final String content) {
    if (StringUtils.hasText(title)) {
      this.title = title;
    }
    this.content = content;
  }

  public void confirmAccess(final Long authorId) {
    if (!this.author.id().equals(authorId)) {
      throw new PrincipalAccessDeniedException();
    }
  }

  public void addComments(final Comment comment) {
    this.comments.add(comment);
    comment.post(this);
  }

}
