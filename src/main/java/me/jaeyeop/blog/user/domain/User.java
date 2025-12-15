package me.jaeyeop.blog.user.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import me.jaeyeop.blog.comment.domain.Comment;
import me.jaeyeop.blog.commons.authentication.OAuth2Attributes;
import me.jaeyeop.blog.commons.authentication.OAuth2Provider;
import me.jaeyeop.blog.commons.persistence.AbstractBaseEntity;
import me.jaeyeop.blog.post.domain.Post;

@Entity
@Getter
public class User extends AbstractBaseEntity {

    @Embedded private UserProfile profile;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OAuth2Provider provider;

    @OneToMany(mappedBy = "author", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private final List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "author", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private final List<Comment> comments = new ArrayList<>();

    protected User() {}

    private User(final UserProfile profile, final Role role, final OAuth2Provider provider) {
        this.profile = profile;
        this.role = role;
        this.provider = provider;
    }

    public static User from(final OAuth2Attributes attributes) {
        return new User(
                new UserProfile(attributes.email(), attributes.name(), attributes.picture()),
                Role.USER,
                attributes.provider());
    }
}
