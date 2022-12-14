package me.jaeyeop.blog.post.adapter.in;

import static me.jaeyeop.blog.post.adapter.in.PostWebAdapter.POST_API_URI;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import me.jaeyeop.blog.post.adapter.out.PostCrudRepository;
import me.jaeyeop.blog.post.domain.Post;
import me.jaeyeop.blog.support.IntegrationTest;
import me.jaeyeop.blog.support.helper.PostHelper;
import me.jaeyeop.blog.support.helper.UserHelper.WithPrincipal;
import me.jaeyeop.blog.user.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author jaeyeopme Created on 12/06/2022.
 */
class PostIntegrationTest extends IntegrationTest {

  @Autowired
  private PostCrudRepository postCrudRepository;

  @WithPrincipal
  @Test
  void 게시글_작성() throws Exception {
    // GIVEN
    final var request = new WritePostRequestDto("title", "content");

    // WHEN
    final var when = mockMvc.perform(post(POST_API_URI)
        .contentType(APPLICATION_JSON)
        .content(toJson(request)));

    // THEN
    when.andExpectAll(status().isCreated());
  }

  @WithPrincipal
  @Test
  void 게시글_조회() throws Exception {
    // GIVEN
    final var author = getPrincipal();
    final var savedPost = getSavedPost(author);
    final var postInformation = new PostInformationProjectionDto(
        savedPost.id(),
        savedPost.information(),
        author.profile().name(),
        savedPost.createdAt(), savedPost.lastModifiedAt());

    // WHEN
    final var when = mockMvc.perform(get(POST_API_URI + "/{id}", savedPost.id()));

    // THEN
    when.andExpectAll(
        status().isOk(),
        content().json(toJson(postInformation)));
  }

  @WithPrincipal
  @Test
  void 게시글_업데이트() throws Exception {
    // GIVEN
    final var savedPost = getSavedPost(getPrincipal());
    final var command = new EditPostRequestDto("newTitle", "newContent");

    // WHEN
    final var when = mockMvc.perform(patch(POST_API_URI + "/{id}", savedPost.id())
        .contentType(APPLICATION_JSON)
        .content(toJson(command)));

    // THEN
    when.andExpectAll(status().isNoContent());
    final var post = postCrudRepository.findById(savedPost.id()).get();
    assertThat(post.id()).isEqualTo(savedPost.id());
    assertThat(post.information().title()).isEqualTo(command.title());
    assertThat(post.information().content()).isEqualTo(command.content());
  }

  @WithPrincipal
  @Test
  void 게시글_삭제() throws Exception {
    // GIVEN
    final var savedPost = getSavedPost(getPrincipal());

    // WHEN
    final var when = mockMvc.perform(delete(POST_API_URI + "/{id}", savedPost.id()));

    // THEN
    when.andExpectAll(status().isNoContent());
    assertThat(postCrudRepository.findById(savedPost.id())).isNotPresent();
  }

  private Post getSavedPost(final User author) {
    final var post = postCrudRepository.save(PostHelper.create(author));
    clearPersistenceContext();
    return post;
  }

}
