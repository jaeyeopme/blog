package me.jaeyeop.blog.user.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import me.jaeyeop.blog.comment.domain.Comment;
import me.jaeyeop.blog.config.jpa.AbstractBaseEntity;
import me.jaeyeop.blog.config.security.authentication.OAuth2Attributes;
import me.jaeyeop.blog.config.security.authentication.OAuth2Provider;
import me.jaeyeop.blog.post.domain.Post;

/**
 * @author jaeyeopme Created on 09/27/2022.
 */
@Entity
@Getter
public class User extends AbstractBaseEntity {

  @Embedded
  private UserProfile profile;

  @NotNull
  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private Role role;

  @NotNull
  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private OAuth2Provider provider;

  @OneToMany(mappedBy = "author")
  private List<Post> posts = new ArrayList<>();

  @OneToMany(mappedBy = "author")
  private List<Comment> comments = new ArrayList<>();

  protected User() {
  }

  private User(
      final UserProfile profile,
      final Role role,
      final OAuth2Provider provider
  ) {
    this.profile = profile;
    this.role = role;
    this.provider = provider;
  }

  public static User from(final OAuth2Attributes attributes) {
    return new User(
        new UserProfile(attributes.email(), attributes.name(), attributes.picture()),
        Role.USER,
        attributes.provider()
    );
  }

}
