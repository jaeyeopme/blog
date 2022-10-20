package me.jaeyeop.blog.post.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import me.jaeyeop.blog.comment.domain.Comment;
import me.jaeyeop.blog.config.error.exception.PrincipalAccessDeniedException;
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
  @JoinColumn(nullable = false, updatable = false)
  private User author;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "post", orphanRemoval = true)
  private List<Comment> comments = new ArrayList<>();

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

  public void updateInformation(final String title,
      final String content) {
    this.title = title;
    this.content = content;
  }

  public void confirmAccess(final Long authorId) {
    if (!this.author.getId().equals(authorId)) {
      throw new PrincipalAccessDeniedException();
    }
  }

  public void addComments(final Comment comment) {
    this.comments.add(comment);
    comment.setPost(this);
  }

}
