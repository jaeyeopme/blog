package me.jaeyeop.blog.comment.adapter.in;

import static me.jaeyeop.blog.comment.adapter.in.CommentRequest.Update;
import static me.jaeyeop.blog.comment.adapter.in.CommentWebAdapter.COMMENT_API_URI;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.List;
import me.jaeyeop.blog.comment.adapter.in.CommentRequest.Create;
import me.jaeyeop.blog.comment.adapter.out.CommentCrudRepository;
import me.jaeyeop.blog.comment.adapter.out.CommentResponse.Info;
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
    final var savedPost = getSavedPost();
    final var command = new Create(savedPost.id(), "content");

    // WHEN
    final var when = mockMvc.perform(post(COMMENT_API_URI)
        .contentType(APPLICATION_JSON)
        .content(toJson(command)));

    // THEN
    when.andExpectAll(status().isCreated());
    final var savedComment = postCrudRepository.findById(savedPost.id()).get().comments().get(0);
    assertThat(savedComment.content()).isEqualTo(command.content());
  }

  @WithPrincipal
  @NullAndEmptySource
  @ParameterizedTest
  void 비어있는_댓글_작성(final String content) throws Exception {
    // GIVEN
    final var savedPost = getSavedPost();
    final var command = new Create(savedPost.id(), content);

    // WHEN
    final var when = mockMvc.perform(post(COMMENT_API_URI)
        .contentType(APPLICATION_JSON)
        .content(toJson(command)));

    // THEN
    when.andExpectAll(status().isBadRequest());
    assertThat(postCrudRepository.findById(savedPost.id()).get().comments()).isEmpty();
  }

  @WithPrincipal
  @Test
  void 댓글_페이지_조회() throws Exception {
    // GIVEN
    final var author = getPrincipal();
    final var savedComment = getSavedComment(author);
    final var info = new Info(savedComment.id(), savedComment.content(), author.profile().name(),
        savedComment.createdAt(),
        savedComment.lastModifiedAt());
    final var pageable = PageRequest.of(0, 2);
    final var infoPage = new PageImpl<>(List.of(info), pageable, 1);

    // WHEN
    final var when = mockMvc.perform(get(COMMENT_API_URI + "/{postId}?page={page}&size={size}",
        savedComment.post().id(), pageable.getPageNumber(), pageable.getPageSize())
        .contentType(APPLICATION_JSON));

    // THEN
    when.andExpectAll(status().isOk(),
        content().json(toJson(infoPage)));
  }

  @WithPrincipal
  @Test
  void 댓글_업데이트() throws Exception {
    // GIVEN
    final var savedComment = getSavedComment(getPrincipal());
    final var command = new Update("newContent");

    // WHEN
    final var when = mockMvc.perform(
        patch(COMMENT_API_URI + "/{commentId}", savedComment.id())
            .contentType(APPLICATION_JSON)
            .content(toJson(command)));

    // THEN
    when.andExpectAll(status().isNoContent());
    final var updatedComment = commentCrudRepository.findById(savedComment.id()).get();
    assertThat(updatedComment.content()).isEqualTo(command.content());
  }

  @WithPrincipal
  @Test
  void 댓글_삭제() throws Exception {
    // GIVEN
    final var savedComment = getSavedComment(getPrincipal());
    assertThat(commentCrudRepository.findById(savedComment.id())).isPresent();

    // WHEN
    final var when = mockMvc.perform(
        delete(COMMENT_API_URI + "/{commentId}", savedComment.id())
            .contentType(APPLICATION_JSON));

    // THEN
    when.andExpectAll(status().isNoContent());
    assertThat(commentCrudRepository.findById(savedComment.id())).isNotPresent();
  }

  private Post getSavedPost() {
    final var post = postCrudRepository.save(PostHelper.create(getPrincipal()));
    clearPersistenceContext();
    return post;
  }

  private Comment getSavedComment(final User author) {
    final var post = postCrudRepository.save(PostHelper.create(author));
    final var comment = CommentHelper.create(author);
    post.addComments(comment);
    clearPersistenceContext();
    return comment;
  }

}
