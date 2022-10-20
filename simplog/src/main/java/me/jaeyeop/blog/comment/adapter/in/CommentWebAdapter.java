package me.jaeyeop.blog.comment.adapter.in;

import javax.validation.Valid;
import me.jaeyeop.blog.comment.adapter.in.command.CreateCommentCommand;
import me.jaeyeop.blog.comment.adapter.out.response.CommentInfo;
import me.jaeyeop.blog.comment.application.port.in.CommentCommandUseCase;
import me.jaeyeop.blog.comment.application.port.in.CommentQueryUseCase;
import me.jaeyeop.blog.config.security.OAuth2UserPrincipal;
import me.jaeyeop.blog.post.adapter.in.command.GetCommentsCommand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

  private final CommentQueryUseCase commentQueryUseCase;

  public CommentWebAdapter(final CommentCommandUseCase commentCommandUseCase,
      final CommentQueryUseCase commentQueryUseCase) {
    this.commentCommandUseCase = commentCommandUseCase;
    this.commentQueryUseCase = commentQueryUseCase;
  }

  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping
  public void create(
      @AuthenticationPrincipal OAuth2UserPrincipal principal,
      @RequestBody @Valid CreateCommentCommand command) {
    commentCommandUseCase.create(principal.getId(), command);
  }

  @ResponseStatus(HttpStatus.OK)
  @GetMapping("/{postId}")
  public Page<CommentInfo> getPage(@PathVariable Long postId,
      Pageable commentsPageable) {
    final GetCommentsCommand command = new GetCommentsCommand(postId, commentsPageable);
    return commentQueryUseCase.getPage(command);
  }

  @ResponseStatus(HttpStatus.OK)
  @PatchMapping("/{id}")
  public void update(
      @AuthenticationPrincipal OAuth2UserPrincipal principal,
      @PathVariable Long id,
      @RequestBody @Valid UpdateCommentCommand command) {
    commentCommandUseCase.update(principal.getId(), id, command);
  }

}
