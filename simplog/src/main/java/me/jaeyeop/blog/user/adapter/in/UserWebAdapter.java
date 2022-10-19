package me.jaeyeop.blog.user.adapter.in;

import javax.validation.Valid;
import me.jaeyeop.blog.config.security.OAuth2UserPrincipal;
import me.jaeyeop.blog.user.adapter.in.command.DeleteUserCommand;
import me.jaeyeop.blog.user.adapter.in.command.GetUserCommand;
import me.jaeyeop.blog.user.adapter.in.command.UpdateUserCommand;
import me.jaeyeop.blog.user.adapter.out.response.UserProfile;
import me.jaeyeop.blog.user.application.port.in.UserCommandUseCase;
import me.jaeyeop.blog.user.application.port.in.UserQueryUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
  public UserProfile getOneByPrincipal(@AuthenticationPrincipal OAuth2UserPrincipal principal) {
    final GetUserCommand command = new GetUserCommand(principal.getEmail());
    return userQueryUseCase.getOneByEmail(command);
  }

  @ResponseStatus(HttpStatus.OK)
  @GetMapping("/{email}")
  public UserProfile getOneByEmail(@PathVariable String email) {
    final GetUserCommand command = new GetUserCommand(email);
    return userQueryUseCase.getOneByEmail(command);
  }

  @ResponseStatus(HttpStatus.OK)
  @PatchMapping
  public void update(@AuthenticationPrincipal OAuth2UserPrincipal principal,
      @RequestBody @Valid UpdateUserCommand command) {
    userCommandUseCase.update(principal.getEmail(), command);
  }

  @ResponseStatus(HttpStatus.OK)
  @DeleteMapping
  public void delete(@AuthenticationPrincipal OAuth2UserPrincipal principal) {
    final DeleteUserCommand command = new DeleteUserCommand(principal.getEmail());
    userCommandUseCase.delete(command);
  }

}
