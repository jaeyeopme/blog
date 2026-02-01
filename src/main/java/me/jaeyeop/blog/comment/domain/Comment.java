package me.jaeyeop.blog.comment.domain;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;

import lombok.Getter;

import me.jaeyeop.blog.commons.error.exception.AccessDeniedException;
import me.jaeyeop.blog.commons.persistence.AbstractBaseEntity;
import me.jaeyeop.blog.post.domain.Post;
import me.jaeyeop.blog.user.domain.User;

@Entity
@Getter
public class Comment extends AbstractBaseEntity {
    @Embedded
    private CommentInformation information;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(nullable = false, updatable = false)
    private User author;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(nullable = false, updatable = false)
    private Post post;

    protected Comment() {}

    private Comment(final Post post, final User author, final CommentInformation information) {
        this.post = post;
        this.information = information;
        this.author = author;
    }

    public static Comment of(
            final Post post, final User author, final CommentInformation information) {
        return new Comment(post, author, information);
    }

    public void confirmAccess(final User author) {
        if (!this.author.equals(author)) {
            throw new AccessDeniedException();
        }
    }
}
