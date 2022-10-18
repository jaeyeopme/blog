package me.jaeyeop.blog.comment.adapter.in;

import javax.validation.Valid;
import me.jaeyeop.blog.comment.application.port.in.CommentCommandUseCase;
import me.jaeyeop.blog.config.security.OAuth2UserPrincipal;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(CommentWebAdapter.COMMENT_API_URI)
@RestController
public class CommentWebAdapter {

  public static final String COMMENT_API_URI = "/api/v1/comments";

  private final CommentCommandUseCase commentCommandUseCase;

  public CommentWebAdapter(final CommentCommandUseCase commentCommandUseCase) {
    this.commentCommandUseCase = commentCommandUseCase;
  }

  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping
  public void create(@AuthenticationPrincipal OAuth2UserPrincipal principal,
      @RequestBody @Valid CreateCommentCommand command) {
    commentCommandUseCase.create(principal.getId(), command);
  }

}
