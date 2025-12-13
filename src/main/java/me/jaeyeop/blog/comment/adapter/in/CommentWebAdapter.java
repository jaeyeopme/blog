package me.jaeyeop.blog.comment.adapter.in;

import static me.jaeyeop.blog.post.adapter.in.PostWebAdapter.POST_API_URI;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.net.URI;
import me.jaeyeop.blog.comment.adapter.out.CommentInformationProjectionDto;
import me.jaeyeop.blog.comment.application.port.in.CommentCommandUseCase;
import me.jaeyeop.blog.comment.application.port.in.CommentCommandUseCase.DeleteCommand;
import me.jaeyeop.blog.comment.application.port.in.CommentCommandUseCase.EditCommand;
import me.jaeyeop.blog.comment.application.port.in.CommentCommandUseCase.WriteCommand;
import me.jaeyeop.blog.comment.application.port.in.CommentQueryUseCase;
import me.jaeyeop.blog.comment.application.port.in.CommentQueryUseCase.PageQuery;
import me.jaeyeop.blog.comment.application.port.in.CommentQueryUseCase.Query;
import me.jaeyeop.blog.commons.config.security.Principal;
import me.jaeyeop.blog.commons.config.security.UserPrincipal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author jaeyeopme Created on 10/18/2022.
 */
@Validated
@RestController
public class CommentWebAdapter implements CommentOAS {

    public static final String COMMENT_API_URI = "/api/v1/comments";

    private final CommentCommandUseCase commentCommandUseCase;

    private final CommentQueryUseCase commentQueryUseCase;

    public CommentWebAdapter(
            final CommentCommandUseCase commentCommandUseCase,
            final CommentQueryUseCase commentQueryUseCase) {
        this.commentCommandUseCase = commentCommandUseCase;
        this.commentQueryUseCase = commentQueryUseCase;
    }

    @ResponseStatus(CREATED)
    @PostMapping(POST_API_URI + "/{postId}/comments")
    @Override
    public ResponseEntity<Void> write(
            @Principal UserPrincipal principal,
            @PathVariable Long postId,
            @RequestBody @Validated WriteCommentRequestDto request) {
        final var command = new WriteCommand(principal.user().id(), postId, request.content());
        final var id = commentCommandUseCase.write(command);
        final var uri = URI.create(String.format("%s/%d", COMMENT_API_URI, id));
        return ResponseEntity.created(uri).build();
    }

    @ResponseStatus(OK)
    @GetMapping(POST_API_URI + "/{postId}/comments")
    @Override
    public Page<CommentInformationProjectionDto> findInformationPageByPostId(
            @PathVariable Long postId,
            @RequestParam(defaultValue = "0") @Min(0) @Max(99) int page,
            @RequestParam(defaultValue = "10") @Min(0) @Max(99) int size) {
        final var query = new PageQuery(postId, PageRequest.of(page, size));
        return commentQueryUseCase.findInformationPageByPostId(query);
    }

    @ResponseStatus(OK)
    @GetMapping(COMMENT_API_URI + "/{commentId}")
    @Override
    public CommentInformationProjectionDto findInformationById(@PathVariable Long commentId) {
        final var query = new Query(commentId);
        return commentQueryUseCase.findInformationById(query);
    }

    @ResponseStatus(NO_CONTENT)
    @PatchMapping(COMMENT_API_URI + "/{commentId}")
    @Override
    public void edit(
            @Principal UserPrincipal principal,
            @PathVariable Long commentId,
            @RequestBody @Validated EditCommentRequestDto request) {
        final var command = new EditCommand(principal.user().id(), commentId, request.content());
        commentCommandUseCase.edit(command);
    }

    @ResponseStatus(NO_CONTENT)
    @DeleteMapping(COMMENT_API_URI + "/{commentId}")
    @Override
    public void delete(@Principal UserPrincipal principal, @PathVariable Long commentId) {
        final var command = new DeleteCommand(principal.user().id(), commentId);
        commentCommandUseCase.delete(command);
    }
}
