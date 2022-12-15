package me.jaeyeop.blog.integration;

import static me.jaeyeop.blog.comment.adapter.in.CommentWebAdapter.COMMENT_API_URI;
import static me.jaeyeop.blog.post.adapter.in.PostWebAdapter.POST_API_URI;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.List;
import me.jaeyeop.blog.comment.adapter.in.EditCommentRequestDto;
import me.jaeyeop.blog.comment.adapter.in.WriteCommentRequestDto;
import me.jaeyeop.blog.comment.adapter.out.CommentCrudRepository;
import me.jaeyeop.blog.comment.adapter.out.CommentInformationProjectionDto;
import me.jaeyeop.blog.comment.domain.Comment;
import me.jaeyeop.blog.post.adapter.out.PostCrudRepository;
import me.jaeyeop.blog.post.domain.Post;
import me.jaeyeop.blog.support.IntegrationTest;
import me.jaeyeop.blog.support.helper.CommentHelper;
import me.jaeyeop.blog.support.helper.PostHelper;
import me.jaeyeop.blog.support.helper.UserHelper.WithPrincipal;
import me.jaeyeop.blog.user.domain.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

/**
 * @author jaeyeopme Created on 12/07/2022.
 */
@SuppressWarnings("OptionalGetWithoutIsPresent")
class CommentIntegrationTest extends IntegrationTest {

  @Autowired
  private CommentCrudRepository commentCrudRepository;

  @Autowired
  private PostCrudRepository postCrudRepository;

  @WithPrincipal
  @Test
  void 댓글_작성() throws Exception {
    // GIVEN
    final var post = getPost(getPrincipal());
    final var request = new WriteCommentRequestDto("content");

    // WHEN
    final var when = mockMvc.perform(
        post(POST_API_URI + "/{postId}/comments", post.id())
            .contentType(APPLICATION_JSON)
            .content(toJson(request))
    );

    // THEN
    when.andExpectAll(status().isCreated());
  }

  @WithPrincipal
  @NullAndEmptySource
  @ParameterizedTest
  void 비어있는_댓글_작성(final String content) throws Exception {
    // GIVEN
    final var post = getPost(getPrincipal());
    final var request = new WriteCommentRequestDto(content);

    // WHEN
    final var when = mockMvc.perform(
        post(POST_API_URI + "/{postId}/comments", post.id())
            .contentType(APPLICATION_JSON)
            .content(toJson(request))
    );

    // THEN
    when.andExpectAll(status().isBadRequest());
  }

  @WithPrincipal
  @Test
  void 댓글_조회() throws Exception {
    // GIVEN
    final var comment = getComment(getPrincipal());
    final var information = new CommentInformationProjectionDto(
        comment.id(),
        comment.author().profile().name(),
        comment.information(),
        comment.createdAt(),
        comment.lastModifiedAt()
    );

    // WHEN
    final var when = mockMvc.perform(
        get(COMMENT_API_URI + "/{commentId}", comment.id())
            .contentType(APPLICATION_JSON)
    );

    // THEN
    when.andExpectAll(
        status().isOk(),
        content().json(toJson(information))
    );
  }

  @WithPrincipal
  @Test
  void 댓글_페이지_조회() throws Exception {
    // GIVEN
    final var comment = getComment(getPrincipal());
    final var info = new CommentInformationProjectionDto(
        comment.id(),
        comment.author().profile().name(),
        comment.information(),
        comment.createdAt(),
        comment.lastModifiedAt()
    );
    final var pageable = PageRequest.of(0, 2);
    final var informationPage = new PageImpl<>(List.of(info), pageable, 1);

    // WHEN
    final var when = mockMvc.perform(
        get(POST_API_URI + "/{postId}/comments?page={page}&size={size}",
            comment.post().id(), pageable.getPageNumber(), pageable.getPageSize())
            .contentType(APPLICATION_JSON)
    );

    // THEN
    when.andExpectAll(
        status().isOk(),
        content().json(toJson(informationPage))
    );
  }

  @WithPrincipal
  @Test
  void 댓글_업데이트() throws Exception {
    // GIVEN
    final var comment = getComment(getPrincipal());
    final var request = new EditCommentRequestDto("newContent");

    // WHEN
    final var when = mockMvc.perform(
        patch(COMMENT_API_URI + "/{commentId}", comment.id())
            .contentType(APPLICATION_JSON)
            .content(toJson(request)));

    // THEN
    when.andExpectAll(status().isNoContent());
    assertThat(commentCrudRepository.findById(comment.id()).get().information().content())
        .isEqualTo(request.content());
  }

  @WithPrincipal
  @Test
  void 댓글_삭제() throws Exception {
    // GIVEN
    final var comment = getComment(getPrincipal());

    // WHEN
    final var when = mockMvc.perform(
        delete(COMMENT_API_URI + "/{commentId}", comment.id())
            .contentType(APPLICATION_JSON));

    // THEN
    when.andExpectAll(status().isNoContent());
    assertThat(commentCrudRepository.findById(comment.id())).isNotPresent();
  }

  private Post getPost(final User author) {
    final var post = postCrudRepository.save(PostHelper.create(author));
    clearPersistenceContext();
    return post;
  }

  private Comment getComment(final User author) {
    final var post = postCrudRepository.save(PostHelper.create(author));
    final var comment = commentCrudRepository.save(CommentHelper.create(post, author));
    clearPersistenceContext();
    return comment;
  }

}
