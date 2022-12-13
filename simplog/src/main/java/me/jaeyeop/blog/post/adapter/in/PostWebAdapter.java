package me.jaeyeop.blog.post.adapter.in;

import static me.jaeyeop.blog.post.adapter.in.PostRequest.Create;
import static me.jaeyeop.blog.post.adapter.in.PostRequest.Delete;
import static me.jaeyeop.blog.post.adapter.in.PostRequest.Find;
import static me.jaeyeop.blog.post.adapter.in.PostRequest.Update;
import static me.jaeyeop.blog.post.adapter.in.PostWebAdapter.POST_API_URI;
import static me.jaeyeop.blog.post.adapter.out.PostResponse.Info;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;
import java.net.URI;
import javax.validation.Valid;
import me.jaeyeop.blog.config.security.authentication.Principal;
import me.jaeyeop.blog.config.security.authentication.UserPrincipal;
import me.jaeyeop.blog.post.application.port.in.PostCommandUseCase;
import me.jaeyeop.blog.post.application.port.in.PostQueryUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author jaeyeopme Created on 10/10/2022.
 */
@Validated
@RequestMapping(POST_API_URI)
@RestController
public class PostWebAdapter implements PostOAS {

  public static final String POST_API_URI = "/api/v1/posts";

  private final PostCommandUseCase postCommandUseCase;

  private final PostQueryUseCase postQueryUseCase;

  public PostWebAdapter(
      final PostCommandUseCase postCommandUseCase,
      final PostQueryUseCase postQueryUseCase) {
    this.postCommandUseCase = postCommandUseCase;
    this.postQueryUseCase = postQueryUseCase;
  }

  @ResponseStatus(NO_CONTENT)
  @DeleteMapping("/{postId}")
  public void delete(
      @Principal UserPrincipal principal,
      @PathVariable Long postId) {
    final var request = new Delete(postId);
    postCommandUseCase.delete(principal.user(), request);
  }

  @ResponseStatus(OK)
  @GetMapping("/{postId}")
  public Info findOne(@PathVariable Long postId) {
    final var request = new Find(postId);
    return postQueryUseCase.findOne(request);
  }


  @ResponseStatus(NO_CONTENT)
  @PatchMapping("/{postId}")
  public void update(
      @Principal UserPrincipal principal,
      @PathVariable Long postId,
      @RequestBody Update request) {
    postCommandUseCase.update(principal.user(), postId, request);
  }

  @PostMapping
  public ResponseEntity<Void> create(
      @Principal UserPrincipal principal,
      @RequestBody @Valid Create request) {
    final var id = postCommandUseCase.create(principal.user(), request);
    final var uri = URI.create(String.format("%s/%d", POST_API_URI, id));
    return ResponseEntity.created(uri).build();
  }

}
