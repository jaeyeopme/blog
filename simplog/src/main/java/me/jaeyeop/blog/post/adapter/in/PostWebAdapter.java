package me.jaeyeop.blog.post.adapter.in;

import java.net.URI;
import javax.validation.Valid;
import me.jaeyeop.blog.config.security.OAuth2UserPrincipal;
import me.jaeyeop.blog.post.adapter.in.command.CreatePostCommand;
import me.jaeyeop.blog.post.application.port.in.PostCommandUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(PostWebAdapter.POST_API_URI)
@RestController
public class PostWebAdapter {

  public static final String POST_API_URI = "/api/v1/posts";

  private final PostCommandUseCase postCommandUseCase;

  public PostWebAdapter(final PostCommandUseCase postCommandUseCase) {
    this.postCommandUseCase = postCommandUseCase;
  }

  @PostMapping
  public ResponseEntity<Void> create(@AuthenticationPrincipal OAuth2UserPrincipal principal,
      @RequestBody @Valid CreatePostCommand command) {
    final Long id = postCommandUseCase.create(principal.getId(), command);
    final URI uri = URI.create(String.format("%s/%d", POST_API_URI, id));
    return ResponseEntity.created(uri).build();
  }

}
