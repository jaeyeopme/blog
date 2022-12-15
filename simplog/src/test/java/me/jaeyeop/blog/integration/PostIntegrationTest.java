package me.jaeyeop.blog.integration;

import static me.jaeyeop.blog.post.adapter.in.PostWebAdapter.POST_API_URI;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import me.jaeyeop.blog.post.adapter.in.EditPostRequestDto;
import me.jaeyeop.blog.post.adapter.in.WritePostRequestDto;
import me.jaeyeop.blog.post.adapter.out.PostCrudRepository;
import me.jaeyeop.blog.post.adapter.out.PostInformationProjectionDto;
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
@SuppressWarnings("OptionalGetWithoutIsPresent")
class PostIntegrationTest extends IntegrationTest {

  @Autowired
  private PostCrudRepository postCrudRepository;

  @WithPrincipal
  @Test
  void 게시글_작성() throws Exception {
    // GIVEN
    final var request = new WritePostRequestDto("title", "content");

    // WHEN
    final var when = mockMvc.perform(
        post(POST_API_URI)
            .contentType(APPLICATION_JSON)
            .content(toJson(request))
    );

    // THEN
    when.andExpectAll(status().isCreated());
  }

  @WithPrincipal
  @Test
  void 게시글_조회() throws Exception {
    // GIVEN
    final var post = getPost(getPrincipal());
    final var information = new PostInformationProjectionDto(
        post.id(),
        post.information(),
        post.author().profile().name(),
        post.createdAt(), post.lastModifiedAt()
    );

    // WHEN
    final var when = mockMvc.perform(
        get(POST_API_URI + "/{id}", post.id())
    );

    // THEN
    when.andExpectAll(
        status().isOk(),
        content().json(toJson(information))
    );
  }

  @WithPrincipal
  @Test
  void 게시글_업데이트() throws Exception {
    // GIVEN
    final var savedPost = getPost(getPrincipal());
    final var request = new EditPostRequestDto("newTitle", "newContent");

    // WHEN
    final var when = mockMvc.perform(
        patch(POST_API_URI + "/{id}", savedPost.id())
            .contentType(APPLICATION_JSON)
            .content(toJson(request))
    );

    // THEN
    when.andExpectAll(status().isNoContent());
    final var post = postCrudRepository.findById(savedPost.id()).get();
    assertThat(post.id()).isEqualTo(savedPost.id());
    assertThat(post.information().title()).isEqualTo(request.title());
    assertThat(post.information().content()).isEqualTo(request.content());
  }

  @WithPrincipal
  @Test
  void 게시글_삭제() throws Exception {
    // GIVEN
    final var post = getPost(getPrincipal());

    // WHEN
    final var when = mockMvc.perform(
        delete(POST_API_URI + "/{id}", post.id())
    );

    // THEN
    when.andExpectAll(status().isNoContent());
    assertThat(postCrudRepository.findById(post.id())).isNotPresent();
  }

  private Post getPost(final User author) {
    final var post = postCrudRepository.save(PostHelper.create(author));
    clearPersistenceContext();
    return post;
  }

}
