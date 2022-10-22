package me.jaeyeop.blog.post.adapter.in;

import static me.jaeyeop.blog.post.adapter.in.PostRequest.Create;
import static me.jaeyeop.blog.post.adapter.in.PostRequest.Delete;
import static me.jaeyeop.blog.post.adapter.in.PostRequest.Find;
import static me.jaeyeop.blog.post.adapter.in.PostRequest.Update;
import static me.jaeyeop.blog.post.adapter.out.PostResponse.Info;
import java.net.URI;
import javax.validation.Valid;
import me.jaeyeop.blog.config.openapi.spec.PostOpenApiSpec;
import me.jaeyeop.blog.config.security.authentication.Principal;
import me.jaeyeop.blog.config.security.authentication.UserPrincipal;
import me.jaeyeop.blog.post.application.port.in.PostCommandUseCase;
import me.jaeyeop.blog.post.application.port.in.PostQueryUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(PostWebAdapter.POST_API_URI)
@RestController
public class PostWebAdapter implements PostOpenApiSpec {

  public static final String POST_API_URI = "/v1/posts";

  private final PostCommandUseCase postCommandUseCase;

  private final PostQueryUseCase postQueryUseCase;

  public PostWebAdapter(
      final PostCommandUseCase postCommandUseCase,
      final PostQueryUseCase postQueryUseCase) {
    this.postCommandUseCase = postCommandUseCase;
    this.postQueryUseCase = postQueryUseCase;
  }

  @PostMapping
  public ResponseEntity<Void> create(
      @Principal UserPrincipal principal,
      @RequestBody @Valid Create request) {
    final var id = postCommandUseCase.create(principal.id(), request);
    final var uri = URI.create(String.format("%s/%d", POST_API_URI, id));
    return ResponseEntity.created(uri).build();
  }

  @ResponseStatus(HttpStatus.OK)
  @GetMapping("/{id}")
  public Info findOne(@PathVariable Long id) {
    final var request = new Find(id);
    return postQueryUseCase.findOne(request);
  }

  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PatchMapping("/{id}")
  public void update(
      @Principal UserPrincipal principal,
      @PathVariable Long id,
      @RequestBody @Valid Update request) {
    postCommandUseCase.update(principal.id(), id, request);
  }

  @ResponseStatus(HttpStatus.NO_CONTENT)
  @DeleteMapping("/{id}")
  public void delete(
      @Principal UserPrincipal principal,
      @PathVariable Long id) {
    final var request = new Delete(id);
    postCommandUseCase.delete(principal.id(), request);
  }

}
