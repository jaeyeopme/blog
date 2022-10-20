package me.jaeyeop.blog.comment.adapter.in;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.Optional;
import me.jaeyeop.blog.comment.adapter.in.command.CreateCommentCommand;
import me.jaeyeop.blog.comment.application.service.CommentCommandService;
import me.jaeyeop.blog.comment.application.service.CommentQueryService;
import me.jaeyeop.blog.config.error.ErrorCode;
import me.jaeyeop.blog.config.error.ErrorResponse;
import me.jaeyeop.blog.config.security.WithUser1;
import me.jaeyeop.blog.post.adapter.out.PostCrudRepository;
import me.jaeyeop.blog.post.domain.PostFactory;
import me.jaeyeop.blog.support.WebMvcTestSupport;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;

@Import({CommentCommandService.class, CommentQueryService.class})
@WebMvcTest(CommentWebAdapter.class)
class CommentWebAdapterTest extends WebMvcTestSupport {

  @Autowired
  private PostCrudRepository postCrudRepository;

  @WithUser1
  @Test
  void 댓글_작성() throws Exception {
    final var postId = 1L;
    final var post = PostFactory.createPost(postId);
    final var command = new CreateCommentCommand(postId, "content");
    given(postCrudRepository.findById(postId)).willReturn(Optional.of(post));

    final var when = mockMvc.perform(
        post(CommentWebAdapter.COMMENT_API_URI).contentType(MediaType.APPLICATION_JSON)
            .content(toJson(command)));

    when.andExpectAll(status().isCreated());
  }

  @WithUser1
  @NullAndEmptySource
  @ParameterizedTest
  void 비어있는_댓글_작성(final String content) throws Exception {
    final var post1Id = 1L;
    final var command = new CreateCommentCommand(post1Id, content);

    final var when = mockMvc.perform(
        post(CommentWebAdapter.COMMENT_API_URI).contentType(MediaType.APPLICATION_JSON)
            .content(toJson(command)));

    when.andExpectAll(status().isBadRequest());
    then(postCrudRepository).should(never()).findById(any());
  }

  @WithUser1
  @Test
  void 존재하지_않는_게시글에_댓글_작성() throws Exception {
    final var postId = 1L;
    final var command = new CreateCommentCommand(postId, "content");
    final var error = ErrorResponse.of(ErrorCode.POST_NOT_FOUND).getBody();
    given(postCrudRepository.findById(postId)).willReturn(Optional.empty());

    final var when = mockMvc.perform(
        post(CommentWebAdapter.COMMENT_API_URI).contentType(MediaType.APPLICATION_JSON)
            .content(toJson(command)));

    when.andExpectAll(status().isNotFound(), content().json(toJson(error)));
  }

}
