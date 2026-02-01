package me.jaeyeop.blog.user.application.port.in;

import me.jaeyeop.blog.user.domain.UserProfile;

public interface UserQueryUseCase {
    UserProfile findProfileById(ProfileQuery profileQuery);

    record ProfileQuery(Long id) {}
}
