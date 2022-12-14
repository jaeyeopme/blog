package me.jaeyeop.blog.user.adapter.in;

import static me.jaeyeop.blog.user.adapter.in.UserWebAdapter.USER_API_URI;
import static me.jaeyeop.blog.user.application.port.in.UserCommandUseCase.DeleteCommand;
import static me.jaeyeop.blog.user.application.port.in.UserCommandUseCase.UpdateCommand;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;
import javax.validation.constraints.Email;
import me.jaeyeop.blog.commons.config.security.Principal;
import me.jaeyeop.blog.commons.config.security.UserPrincipal;
import me.jaeyeop.blog.user.application.port.in.UserCommandUseCase;
import me.jaeyeop.blog.user.application.port.in.UserQueryUseCase;
import me.jaeyeop.blog.user.application.port.in.UserQueryUseCase.ProfileQuery;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author jaeyeopme Created on 10/06/2022.
 */
@Validated
@RequestMapping(USER_API_URI)
@RestController
public class UserWebAdapter implements UserOAS {

  public static final String USER_API_URI = "/api/v1/users";

  private final UserCommandUseCase userCommandUseCase;

  private final UserQueryUseCase userQueryUseCase;

  public UserWebAdapter(
      final UserCommandUseCase userCommandUseCase,
      final UserQueryUseCase userQueryUseCase
  ) {
    this.userCommandUseCase = userCommandUseCase;
    this.userQueryUseCase = userQueryUseCase;
  }

  @ResponseStatus(OK)
  @GetMapping("/me")
  @Override
  public UserProfileResponseDto findById(@Principal UserPrincipal principal) {
    final var query = new ProfileQuery(principal.user().profile().email());
    final var profile = userQueryUseCase.findProfileByEmail(query);
    return UserProfileResponseDto.from(profile);
  }

  @ResponseStatus(OK)
  @GetMapping("/{email}")
  @Override
  public UserProfileResponseDto findByEmail(@PathVariable @Email String email) {
    final var query = new ProfileQuery(email);
    final var profile = userQueryUseCase.findProfileByEmail(query);
    return UserProfileResponseDto.from(profile);
  }

  @ResponseStatus(NO_CONTENT)
  @PatchMapping("/me")
  @Override
  public void update(
      @Principal UserPrincipal principal,
      @RequestBody UpdateUserRequestDto request
  ) {
    final var command = new UpdateCommand(
        principal.user().id(),
        request.name(),
        request.introduce());
    userCommandUseCase.update(command);
  }

  @ResponseStatus(NO_CONTENT)
  @DeleteMapping("/me")
  @Override
  public void delete(@Principal UserPrincipal principal) {
    final var command = new DeleteCommand(principal.user().id());
    userCommandUseCase.delete(command);
  }

}
