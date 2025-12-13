package me.jaeyeop.blog.post.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import org.springframework.util.StringUtils;

/**
 * @author jaeyeopme Created on 12/11/2022.
 */
@Embeddable
@Getter
public class PostInformation {

    @NotBlank
    @Column(nullable = false)
    private String title;

    @NotBlank
    @Lob
    @Column(nullable = false)
    private String content;

    @Column private String coverImage;

    protected PostInformation() {}

    public PostInformation(final String title, final String content) {
        this.title = title;
        this.content = content;
    }

    public void edit(final String newTitle, final String newContent) {
        if (StringUtils.hasText(newTitle)) {
            this.title = newTitle;
        }

        if (StringUtils.hasText(newContent)) {
            this.content = newContent;
        }
    }
}
