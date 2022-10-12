package me.jaeyeop.blog.user.adapter.in;

import javax.validation.Valid;
import me.jaeyeop.blog.config.security.OAuth2UserPrincipal;
import me.jaeyeop.blog.user.adapter.in.command.DeleteUserProfileCommand;
import me.jaeyeop.blog.user.adapter.in.command.GetUserProfileCommand;
import me.jaeyeop.blog.user.adapter.in.command.UpdateUserProfileCommand;
import me.jaeyeop.blog.user.adapter.in.response.UserProfile;
import me.jaeyeop.blog.user.application.port.in.UserCommandUseCase;
import me.jaeyeop.blog.user.application.port.in.UserQueryUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(UserWebAdapter.USER_API_URI)
@RestController
public class UserWebAdapter {

  public static final String USER_API_URI = "/api/v1/users";

  private final UserCommandUseCase userCommandUseCase;

  private final UserQueryUseCase userQueryUseCase;

  public UserWebAdapter(final UserCommandUseCase userCommandUseCase,
      final UserQueryUseCase userQueryUseCase) {
    this.userCommandUseCase = userCommandUseCase;
    this.userQueryUseCase = userQueryUseCase;
  }

  @ResponseStatus(HttpStatus.OK)
  @GetMapping
  public UserProfile getProfile(@AuthenticationPrincipal OAuth2UserPrincipal principal) {
    final GetUserProfileCommand command = new GetUserProfileCommand(principal.getName());
    return userQueryUseCase.getProfile(command);
  }

  @ResponseStatus(HttpStatus.OK)
  @GetMapping("/{email}")
  public UserProfile getProfile(@PathVariable String email) {
    final GetUserProfileCommand command = new GetUserProfileCommand(email);
    return userQueryUseCase.getProfile(command);
  }

  @ResponseStatus(HttpStatus.OK)
  @PutMapping
  public UserProfile updateProfile(@AuthenticationPrincipal OAuth2UserPrincipal principal,
      @RequestBody @Valid UpdateUserProfileCommand command) {
    return userCommandUseCase.updateProfile(principal.getName(), command);
  }

  @ResponseStatus(HttpStatus.OK)
  @DeleteMapping
  public void deleteProfile(@AuthenticationPrincipal OAuth2UserPrincipal principal) {
    final DeleteUserProfileCommand command = new DeleteUserProfileCommand(principal.getName());
    userCommandUseCase.deleteProfile(command);
  }

}
