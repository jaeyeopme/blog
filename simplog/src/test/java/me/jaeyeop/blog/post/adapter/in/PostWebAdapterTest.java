package me.jaeyeop.blog.post.adapter.in;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.Optional;
import me.jaeyeop.blog.config.error.ErrorCode;
import me.jaeyeop.blog.config.error.ErrorResponse;
import me.jaeyeop.blog.config.security.WithUser1;
import me.jaeyeop.blog.post.adapter.in.command.CreatePostCommand;
import me.jaeyeop.blog.post.adapter.in.command.UpdatePostCommand;
import me.jaeyeop.blog.post.adapter.out.PostCrudRepository;
import me.jaeyeop.blog.post.adapter.out.PostQueryRepository;
import me.jaeyeop.blog.post.application.service.PostCommandService;
import me.jaeyeop.blog.post.application.service.PostQueryService;
import me.jaeyeop.blog.post.domain.PostFactory;
import me.jaeyeop.blog.support.WebMvcTestSupport;
import me.jaeyeop.blog.user.domain.UserFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;

@SuppressWarnings("deprecation")
@Import({PostCommandService.class, PostQueryService.class})
@WebMvcTest(PostWebAdapter.class)
class PostWebAdapterTest extends WebMvcTestSupport {

  @Autowired
  private PostCrudRepository postCrudRepository;

  @Autowired
  private PostQueryRepository postQueryRepository;

  @WithUser1
  @Test
  void 게시글_작성() throws Exception {
    final var command = new CreatePostCommand("title", "content");
    final var post1 = PostFactory.createPost1();
    given(postCrudRepository.save(any())).willReturn(post1);

    final var when = mockMvc.perform(
        post(PostWebAdapter.POST_API_URI)
            .contentType(APPLICATION_JSON_UTF8)
            .content(toJson(command)));

    final var createdURI = String.format("%s/%d", PostWebAdapter.POST_API_URI, post1.getId());
    when.andExpectAll(
        status().isCreated(),
        header().string(HttpHeaders.LOCATION, createdURI));
  }

  @WithUser1
  @NullAndEmptySource
  @ParameterizedTest
  void 제목이_비어있는_게시글_작성(final String title) throws Exception {
    final var command = new CreatePostCommand(title, "content");

    final var when = mockMvc.perform(
        post(PostWebAdapter.POST_API_URI)
            .contentType(APPLICATION_JSON_UTF8)
            .content(toJson(command)));

    when.andExpectAll(status().isBadRequest());
  }

  @Test
  void 게시글_조회() throws Exception {
    final var id = 1L;
    final var information = PostFactory.createInformation();
    given(postQueryRepository.getPostInformationById(id)).willReturn(Optional.of(information));

    final var when = mockMvc.perform(
        get(PostWebAdapter.POST_API_URI + "/{id}", id));

    when.andExpectAll(
        status().isOk(),
        content().json(toJson(information)));
  }

  @Test
  void 존재하지_않는_게시글_조회() throws Exception {
    final var id = 1L;
    final var error = ErrorResponse.of(ErrorCode.POST_NOT_FOUND).getBody();
    given(postQueryRepository.getPostInformationById(id)).willReturn(Optional.empty());

    final var when = mockMvc.perform(
        get(PostWebAdapter.POST_API_URI + "/{id}", id));

    when.andExpectAll(
        status().isNotFound(),
        content().json(toJson(error)));
  }

  @WithUser1
  @Test
  void 게시글_업데이트() throws Exception {
    final var id = 1L;
    final var command = new UpdatePostCommand("newTitle", "newContent");
    final var post = PostFactory.createPost1(UserFactory.createUser1());
    given(postCrudRepository.findById(id)).willReturn(Optional.of(post));

    final var when = mockMvc.perform(
        patch(PostWebAdapter.POST_API_URI + "/{id}", id)
            .contentType(APPLICATION_JSON_UTF8)
            .content(toJson(command)));

    when.andExpectAll(status().isOk());
  }

  @WithUser1
  @NullAndEmptySource
  @ParameterizedTest
  void 비어있는_제목으로_게시글_업데이트(final String title) throws Exception {
    final var id = 1L;
    final var command = new UpdatePostCommand(title, "newContent");

    final var when = mockMvc.perform(
        patch(PostWebAdapter.POST_API_URI + "/{id}", id)
            .contentType(APPLICATION_JSON_UTF8)
            .content(toJson(command)));

    when.andExpectAll(status().isBadRequest());
  }

  @WithUser1
  @Test
  void 존재하지_않는_게시글_업데이트() throws Exception {
    final var id = 1L;
    final var command = new UpdatePostCommand("newTitle", "newContent");
    final var error = ErrorResponse.of(ErrorCode.POST_NOT_FOUND).getBody();
    given(postCrudRepository.findById(id)).willReturn(Optional.empty());

    final var when = mockMvc.perform(
        patch(PostWebAdapter.POST_API_URI + "/{id}", id)
            .contentType(APPLICATION_JSON_UTF8)
            .content(toJson(command)));

    when.andExpectAll(
        status().isNotFound(),
        content().json(toJson(error)));
  }

  @WithUser1
  @Test
  void 다른_사람의_게시글_업데이트() throws Exception {
    final var id = 1L;
    final var command = new UpdatePostCommand("newTitle", "newContent");
    final var user2 = UserFactory.createUser2();
    final var post1 = PostFactory.createPost1(user2);
    final var error = ErrorResponse.of(ErrorCode.FORBIDDEN).getBody();
    given(postCrudRepository.findById(id)).willReturn(Optional.of(post1));

    final var when = mockMvc.perform(
        patch(PostWebAdapter.POST_API_URI + "/{id}", id)
            .contentType(APPLICATION_JSON_UTF8)
            .content(toJson(command)));

    when.andExpectAll(
        status().isForbidden(),
        content().json(toJson(error)));
  }

  @WithUser1
  @Test
  void 게시글_삭제() throws Exception {
    final var id = 1L;
    final var post1 = PostFactory.createPost1(UserFactory.createUser1());
    given(postCrudRepository.findById(id)).willReturn(Optional.of(post1));

    final var when = mockMvc.perform(
        delete(PostWebAdapter.POST_API_URI + "/{id}", id));

    when.andExpectAll(status().isOk());
  }

  @WithUser1
  @Test
  void 존재하지_않는_게시글_삭제() throws Exception {
    final var id = 1L;
    final var error = ErrorResponse.of(ErrorCode.POST_NOT_FOUND).getBody();
    given(postCrudRepository.findById(id)).willReturn(Optional.empty());

    final var when = mockMvc.perform(
        delete(PostWebAdapter.POST_API_URI + "/{id}", id));

    when.andExpectAll(
        status().isNotFound(),
        content().json(toJson(error)));
  }

  @WithUser1
  @Test
  void 다른_사람의_게시글_삭제() throws Exception {
    final var id = 1L;
    final var user2 = UserFactory.createUser2();
    final var post1 = PostFactory.createPost1(user2);
    final var error = ErrorResponse.of(ErrorCode.FORBIDDEN).getBody();
    given(postCrudRepository.findById(id)).willReturn(Optional.of(post1));

    final var when = mockMvc.perform(
        delete(PostWebAdapter.POST_API_URI + "/{id}", id));

    when.andExpectAll(
        status().isForbidden(),
        content().json(toJson(error)));
  }

}
