package me.jaeyeop.blog.post.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import me.jaeyeop.blog.comment.domain.Comment;
import me.jaeyeop.blog.commons.error.exception.AccessDeniedException;
import me.jaeyeop.blog.commons.persistence.AbstractBaseEntity;
import me.jaeyeop.blog.user.domain.User;

/**
 * @author jaeyeopme Created on 10/10/2022.
 */
@Entity
@Getter
public class Post extends AbstractBaseEntity {

    @Embedded private PostInformation information;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(nullable = false, updatable = false)
    private User author;

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private final List<Comment> comments = new ArrayList<>();

    protected Post() {}

    private Post(final User author, final PostInformation information) {
        this.information = information;
        this.author = author;
    }

    public static Post of(final User author, final PostInformation information) {
        return new Post(author, information);
    }

    public void confirmAccess(final User author) {
        if (!this.author().equals(author)) {
            throw new AccessDeniedException();
        }
    }
}
