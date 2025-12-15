package me.jaeyeop.blog.post.adapter.in;

import static me.jaeyeop.blog.post.adapter.in.PostWebAdapter.POST_API_URI;
import static me.jaeyeop.blog.post.application.port.in.PostCommandUseCase.DeleteCommand;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

import java.net.URI;
import me.jaeyeop.blog.commons.config.security.Principal;
import me.jaeyeop.blog.commons.config.security.UserPrincipal;
import me.jaeyeop.blog.post.adapter.out.PostInformationProjectionDto;
import me.jaeyeop.blog.post.application.port.in.PostCommandUseCase;
import me.jaeyeop.blog.post.application.port.in.PostCommandUseCase.EditCommand;
import me.jaeyeop.blog.post.application.port.in.PostCommandUseCase.WriteCommand;
import me.jaeyeop.blog.post.application.port.in.PostQueryUseCase;
import me.jaeyeop.blog.post.application.port.in.PostQueryUseCase.InformationQuery;
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

@Validated
@RequestMapping(POST_API_URI)
@RestController
public class PostWebAdapter implements PostOAS {

    public static final String POST_API_URI = "/api/v1/posts";

    private final PostCommandUseCase postCommandUseCase;

    private final PostQueryUseCase postQueryUseCase;

    public PostWebAdapter(
            final PostCommandUseCase postCommandUseCase, final PostQueryUseCase postQueryUseCase) {
        this.postCommandUseCase = postCommandUseCase;
        this.postQueryUseCase = postQueryUseCase;
    }

    @ResponseStatus(NO_CONTENT)
    @DeleteMapping("/{postId}")
    public void delete(@Principal UserPrincipal principal, @PathVariable Long postId) {
        final var command = new DeleteCommand(principal.user().id(), postId);
        postCommandUseCase.delete(command);
    }

    @ResponseStatus(OK)
    @GetMapping("/{postId}")
    public PostInformationProjectionDto findInformationById(@PathVariable Long postId) {
        final var query = new InformationQuery(postId);
        return postQueryUseCase.findInformationById(query);
    }

    @ResponseStatus(NO_CONTENT)
    @PatchMapping("/{postId}")
    public void edit(
            @Principal UserPrincipal principal,
            @PathVariable Long postId,
            @RequestBody EditPostRequestDto request) {
        final var command =
                new EditCommand(principal.user().id(), postId, request.title(), request.content());
        postCommandUseCase.edit(command);
    }

    @PostMapping
    public ResponseEntity<Void> write(
            @Principal UserPrincipal principal,
            @RequestBody @Validated WritePostRequestDto request) {
        final var command =
                new WriteCommand(principal.user().id(), request.title(), request.content());
        final var id = postCommandUseCase.write(command);
        final var uri = URI.create(String.format("%s/%d", POST_API_URI, id));
        return ResponseEntity.created(uri).build();
    }
}
