package me.jaeyeop.blog.post.adapter.in;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.Optional;
import me.jaeyeop.blog.config.error.ErrorCode;
import me.jaeyeop.blog.config.error.ErrorResponse;
import me.jaeyeop.blog.config.security.WithDefaultUser;
import me.jaeyeop.blog.config.support.WebMvcTestSupport;
import me.jaeyeop.blog.post.adapter.in.command.CreatePostCommand;
import me.jaeyeop.blog.post.adapter.out.PostCommandRepository;
import me.jaeyeop.blog.post.adapter.out.PostQueryRepository;
import me.jaeyeop.blog.post.application.service.PostCommandService;
import me.jaeyeop.blog.post.application.service.PostQueryService;
import me.jaeyeop.blog.post.domain.PostFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.security.test.context.support.WithAnonymousUser;

@SuppressWarnings("deprecation")
@Import({PostCommandService.class, PostQueryService.class})
@WebMvcTest(PostWebAdapter.class)
class PostWebAdapterTest extends WebMvcTestSupport {

  @MockBean
  private PostCommandRepository postCommandRepository;

  @MockBean
  private PostQueryRepository postQueryRepository;

  @WithDefaultUser
  @Test
  void 게시글_작성() throws Exception {
    final var post = PostFactory.createDefault();
    final var command = new CreatePostCommand("title", "content");
    given(postCommandRepository.save(any())).willReturn(post);

    final var when = mockMvc.perform(post(PostWebAdapter.POST_API_URI)
        .contentType(APPLICATION_JSON_UTF8)
        .content(toJson(command)));

    final var createdURI = String.format("%s/%d", PostWebAdapter.POST_API_URI, post.getId());
    when.andExpectAll(status().isCreated(),
        header().string(HttpHeaders.LOCATION, createdURI));
  }

  @WithDefaultUser
  @NullAndEmptySource
  @ParameterizedTest
  void 제목이_비어있는_게시글_작성(final String title) throws Exception {
    final var command = new CreatePostCommand(title, "content");

    final var when = mockMvc.perform(post(PostWebAdapter.POST_API_URI)
        .contentType(APPLICATION_JSON_UTF8)
        .content(toJson(command)));

    when.andExpectAll(status().isBadRequest());
  }

  @WithAnonymousUser
  @Test
  void 게시글_조회() throws Exception {
    final var id = 1L;
    final var expected = PostFactory.createInformation();
    given(postQueryRepository.getInformationById(id))
        .willReturn(Optional.of(expected));

    final var when = mockMvc.perform(get(PostWebAdapter.POST_API_URI + "/{id}", id));

    when.andExpectAll(status().isOk(),
        content().json(toJson(expected)));
  }

  @WithAnonymousUser
  @Test
  void 존재하지_않는_게시글_조회() throws Exception {
    final var id = 2L;
    final var error = ErrorResponse.of(ErrorCode.POST_NOT_FOUND).getBody();
    given(postQueryRepository.getInformationById(id)).willReturn(Optional.empty());

    final var when = mockMvc.perform(get(PostWebAdapter.POST_API_URI + "/{id}", id));

    when.andExpectAll(status().isNotFound(),
        content().json(toJson(error)));
  }

}