package me.jaeyeop.blog.comment.adapter.in;

import static me.jaeyeop.blog.comment.adapter.in.CommentRequest.Create;
import static me.jaeyeop.blog.comment.adapter.in.CommentRequest.Update;
import static me.jaeyeop.blog.comment.adapter.out.CommentResponse.Info;
import javax.validation.Valid;
import me.jaeyeop.blog.comment.adapter.in.CommentRequest.Delete;
import me.jaeyeop.blog.comment.adapter.in.CommentRequest.Find;
import me.jaeyeop.blog.comment.application.port.in.CommentCommandUseCase;
import me.jaeyeop.blog.comment.application.port.in.CommentQueryUseCase;
import me.jaeyeop.blog.config.openapi.spec.CommentOpenApiSpec;
import me.jaeyeop.blog.config.security.authentication.Principal;
import me.jaeyeop.blog.config.security.authentication.UserPrincipal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
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
public class CommentWebAdapter implements CommentOpenApiSpec {

  public static final String COMMENT_API_URI = "/v1/comments";

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
      @Principal UserPrincipal principal,
      @RequestBody @Valid Create request) {
    commentCommandUseCase.create(principal.id(), request);
  }

  @ResponseStatus(HttpStatus.OK)
  @GetMapping("/{postId}")
  public Page<Info> findPage(
      @PathVariable Long postId,
      Pageable commentPageable) {
    final var request = new Find(postId, commentPageable);
    return commentQueryUseCase.findCommentPage(request);
  }

  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PatchMapping("/{id}")
  public void update(
      @Principal UserPrincipal principal,
      @PathVariable Long id,
      @RequestBody @Valid Update request) {
    commentCommandUseCase.update(principal.id(), id, request);
  }

  @ResponseStatus(HttpStatus.NO_CONTENT)
  @DeleteMapping("/{id}")
  public void delete(
      @Principal UserPrincipal principal,
      @PathVariable Long id) {
    final var request = new Delete(id);
    commentCommandUseCase.delete(principal.id(), request);
  }

}
