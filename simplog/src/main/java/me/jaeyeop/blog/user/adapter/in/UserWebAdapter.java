package me.jaeyeop.blog.user.adapter.in;

import static me.jaeyeop.blog.user.adapter.in.UserRequest.Update;
import javax.validation.constraints.Email;
import me.jaeyeop.blog.config.oas.spec.UserOAS;
import me.jaeyeop.blog.config.security.authentication.Principal;
import me.jaeyeop.blog.config.security.authentication.UserPrincipal;
import me.jaeyeop.blog.user.adapter.in.UserRequest.Delete;
import me.jaeyeop.blog.user.adapter.in.UserRequest.Find;
import me.jaeyeop.blog.user.adapter.out.UserResponse.Profile;
import me.jaeyeop.blog.user.application.port.in.UserCommandUseCase;
import me.jaeyeop.blog.user.application.port.in.UserQueryUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RequestMapping(UserWebAdapter.USER_API_URI)
@RestController
public class UserWebAdapter implements UserOAS {

  public static final String USER_API_URI = "/v1/users";

  private final UserCommandUseCase userCommandUseCase;

  private final UserQueryUseCase userQueryUseCase;

  public UserWebAdapter(
      final UserCommandUseCase userCommandUseCase,
      final UserQueryUseCase userQueryUseCase) {
    this.userCommandUseCase = userCommandUseCase;
    this.userQueryUseCase = userQueryUseCase;
  }

  @ResponseStatus(HttpStatus.NO_CONTENT)
  @DeleteMapping
  @Override
  public void delete(@Principal UserPrincipal principal) {
    final var request = new Delete(principal.email());
    userCommandUseCase.delete(request);
  }

  @ResponseStatus(HttpStatus.OK)
  @GetMapping
  @Override
  public Profile findByPrincipal(@Principal UserPrincipal principal) {
    final var request = new Find(principal.email());
    return userQueryUseCase.findOneByEmail(request);
  }

  @ResponseStatus(HttpStatus.OK)
  @GetMapping("/{email}")
  @Override
  public Profile findOneByEmail(@PathVariable @Email String email) {
    final var request = new Find(email);
    return userQueryUseCase.findOneByEmail(request);
  }

  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PatchMapping
  @Override
  public void update(
      @Principal UserPrincipal principal,
      @RequestBody Update request) {
    userCommandUseCase.update(principal.email(), request);
  }

}
