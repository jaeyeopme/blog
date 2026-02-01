package me.jaeyeop.blog.comment.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;

import lombok.Getter;

@Embeddable
@Getter
public class CommentInformation {
    @NotBlank
    @Column(nullable = false)
    private String content;

    protected CommentInformation() {}

    public CommentInformation(final String content) {
        this.content = content;
    }

    public void edit(final String newContent) {
        this.content = newContent;
    }
}
