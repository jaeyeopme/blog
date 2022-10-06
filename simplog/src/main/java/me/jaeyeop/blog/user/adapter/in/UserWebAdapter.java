package me.jaeyeop.blog.user.adapter.in;

import lombok.RequiredArgsConstructor;
import me.jaeyeop.blog.config.security.OAuth2UserPrincipal;
import me.jaeyeop.blog.user.application.port.in.UserProfile;
import me.jaeyeop.blog.user.application.port.in.UserQueryUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(UserWebAdapter.USER_API_URI)
@RestController
@RequiredArgsConstructor
public class UserWebAdapter {

  public static final String USER_API_URI = "/api/v1/users";

  private final UserQueryUseCase userQueryUseCase;

  @ResponseStatus(HttpStatus.OK)
  @GetMapping
  public UserProfile getProfile(
      @AuthenticationPrincipal OAuth2UserPrincipal principal) {
    return UserProfile.from(principal.getUser());
  }

  @ResponseStatus(HttpStatus.OK)
  @GetMapping("/{email}")
  public UserProfile getProfile(@PathVariable String email) {
    final var command = new GetProfileCommand(email);
    return userQueryUseCase.getProfile(command);
  }

}
