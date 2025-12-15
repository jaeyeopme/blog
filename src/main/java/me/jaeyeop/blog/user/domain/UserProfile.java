package me.jaeyeop.blog.user.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import org.springframework.util.StringUtils;

@Getter
@Embeddable
public class UserProfile {

    @NotBlank
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank
    @Column(nullable = false)
    private String name;

    @Column private String picture;

    @Column private String introduce;

    protected UserProfile() {}

    public UserProfile(final String email, final String name, final String picture) {
        this.email = email;
        this.name = name;
        this.picture = picture;
    }

    public void update(final String newName, final String newIntroduce) {
        if (StringUtils.hasText(newName)) {
            this.name = newName;
        }

        if (StringUtils.hasText(newIntroduce)) {
            this.introduce = newIntroduce;
        }
    }
}
