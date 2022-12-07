package me.jaeyeop.blog.user.adapter.in;

import static me.jaeyeop.blog.user.adapter.in.UserRequest.Update;
import static me.jaeyeop.blog.user.adapter.in.UserWebAdapter.USER_API_URI;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;
import javax.validation.constraints.Email;
import me.jaeyeop.blog.config.oas.spec.UserOAS;
import me.jaeyeop.blog.config.security.authentication.Principal;
import me.jaeyeop.blog.config.security.authentication.UserPrincipal;
import me.jaeyeop.blog.user.adapter.in.UserRequest.Delete;
import me.jaeyeop.blog.user.adapter.in.UserRequest.Find;
import me.jaeyeop.blog.user.adapter.out.UserResponse.Profile;
import me.jaeyeop.blog.user.application.port.in.UserCommandUseCase;
import me.jaeyeop.blog.user.application.port.in.UserQueryUseCase;
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
      final UserQueryUseCase userQueryUseCase) {
    this.userCommandUseCase = userCommandUseCase;
    this.userQueryUseCase = userQueryUseCase;
  }

  @ResponseStatus(NO_CONTENT)
  @DeleteMapping
  @Override
  public void delete(@Principal UserPrincipal principal) {
    final var request = new Delete(principal.user().id());
    userCommandUseCase.delete(request);
  }

  @ResponseStatus(OK)
  @GetMapping
  @Override
  public Profile findByPrincipal(@Principal UserPrincipal principal) {
    final var request = new Find(principal.user().email());
    return userQueryUseCase.findOneByEmail(request);
  }

  @ResponseStatus(OK)
  @GetMapping("/{email}")
  @Override
  public Profile findOneByEmail(@PathVariable @Email String email) {
    final var request = new Find(email);
    return userQueryUseCase.findOneByEmail(request);
  }

  @ResponseStatus(NO_CONTENT)
  @PatchMapping
  @Override
  public void update(
      @Principal UserPrincipal principal,
      @RequestBody Update request) {
    userCommandUseCase.update(principal.user().id(), request);
  }

}
