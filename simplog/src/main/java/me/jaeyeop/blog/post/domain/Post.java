package me.jaeyeop.blog.post.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import me.jaeyeop.blog.comment.domain.Comment;
import me.jaeyeop.blog.config.error.exception.AccessDeniedException;
import me.jaeyeop.blog.config.jpa.AbstractBaseEntity;
import me.jaeyeop.blog.user.domain.User;

/**
 * @author jaeyeopme Created on 10/10/2022.
 */
@Entity
@Getter
public class Post extends AbstractBaseEntity {

  @Embedded
  private PostInformation information;

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(nullable = false, updatable = false)
  private User author;

  @OneToMany(mappedBy = "post")
  private List<Comment> comments = new ArrayList<>();

  protected Post() {
  }

  private Post(
      final User author,
      final PostInformation information
  ) {
    this.information = information;
    this.author = author;
  }

  public static Post of(
      final User author,
      final PostInformation information
  ) {
    return new Post(author, information);
  }

  public void confirmAccess(final User author) {
    if (!this.author().equals(author)) {
      throw new AccessDeniedException();
    }
  }

}
