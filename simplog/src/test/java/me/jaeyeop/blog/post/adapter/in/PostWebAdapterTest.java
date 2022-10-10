package me.jaeyeop.blog.post.adapter.in;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import me.jaeyeop.blog.config.security.WithDefaultUser;
import me.jaeyeop.blog.config.support.WebMvcTestSupport;
import me.jaeyeop.blog.post.adapter.out.PostRepository;
import me.jaeyeop.blog.post.application.service.PostCommandService;
import me.jaeyeop.blog.post.domain.PostFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;

@SuppressWarnings("deprecation")
@Import({PostCommandService.class})
@WebMvcTest(PostWebAdapter.class)
class PostWebAdapterTest extends WebMvcTestSupport {

  @Autowired
  private PostRepository postRepository;

  @WithDefaultUser
  @Test
  void 게시글_작성() throws Exception {
    final var post = PostFactory.createDefault();
    given(postRepository.save(any())).willReturn(post);
    final var command = new CreatePostCommand("title", "content");

    final var given = post(PostWebAdapter.POST_API_URI)
        .contentType(APPLICATION_JSON_UTF8)
        .content(toJson(command));

    final var when = mockMvc.perform(given);

    final var createdURI = String.format("%s/%d", PostWebAdapter.POST_API_URI, post.getId());
    when.andExpectAll(status().isCreated(),
        header().string(HttpHeaders.LOCATION, createdURI));
  }

  @WithDefaultUser
  @NullAndEmptySource
  @ParameterizedTest
  void 제목이_비어있는_게시글_작성(final String title) throws Exception {
    final var command = new CreatePostCommand(title, "content");
    final var given = post(PostWebAdapter.POST_API_URI)
        .contentType(APPLICATION_JSON_UTF8)
        .content(toJson(command));

    final var when = mockMvc.perform(given);

    when.andExpectAll(status().isBadRequest());
    then(postRepository).should(never()).save(any());
  }

}