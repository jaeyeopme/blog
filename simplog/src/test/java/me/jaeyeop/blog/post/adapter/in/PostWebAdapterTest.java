//package me.jaeyeop.blog.unit.post.adapter.in;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.BDDMockito.given;
//import static org.mockito.BDDMockito.then;
//import static org.mockito.Mockito.never;
//import static org.springframework.http.MediaType.APPLICATION_JSON;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//import java.util.Optional;
//import me.jaeyeop.blog.config.error.Error;
//import me.jaeyeop.blog.config.error.ErrorResponse;
//import me.jaeyeop.blog.config.security.WithUser1;
//import me.jaeyeop.blog.post.adapter.in.PostRequest.Create;
//import me.jaeyeop.blog.post.adapter.in.PostRequest.Update;
//import me.jaeyeop.blog.post.adapter.in.PostWebAdapter;
//import me.jaeyeop.blog.post.adapter.out.PostCrudRepository;
//import me.jaeyeop.blog.post.adapter.out.PostQueryRepository;
//import me.jaeyeop.blog.post.application.service.PostCommandService;
//import me.jaeyeop.blog.post.application.service.PostQueryService;
//import me.jaeyeop.blog.support.WebMvcTestSupport;
//import me.jaeyeop.blog.unit.post.domain.PostFactory;
//import me.jaeyeop.blog.unit.user.domain.UserFactory;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.params.ParameterizedTest;
//import org.junit.jupiter.params.provider.NullAndEmptySource;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.context.annotation.Import;
//import org.springframework.http.HttpHeaders;
//
//@Import({PostCommandService.class, PostQueryService.class})
//@WebMvcTest(PostWebAdapter.class)
//class PostWebAdapterTest extends WebMvcTestSupport {
//
//  @Autowired
//  private PostCrudRepository postCrudRepository;
//
//  @Autowired
//  private PostQueryRepository postQueryRepository;
//
//  @WithUser1
//  @Test
//  void 게시글_작성() throws Exception {
//    final var postId = 1L;
//    final var command = new Create("title", "content");
//    final var post = PostFactory.createPost(postId);
//    given(postCrudRepository.save(any())).willReturn(post);
//
//    final var when = mockMvc.perform(
//        post(PostWebAdapter.POST_API_URI)
//            .contentType(APPLICATION_JSON)
//            .content(toJson(command)));
//
//    final var createdURI = String.format("%s/%d", PostWebAdapter.POST_API_URI, postId);
//    when.andExpectAll(
//        status().isCreated(),
//        header().string(HttpHeaders.LOCATION, createdURI));
//  }
//
//  @WithUser1
//  @NullAndEmptySource
//  @ParameterizedTest
//  void 제목이_비어있는_게시글_작성(final String title) throws Exception {
//    final var command = new Create(title, "content");
//
//    final var when = mockMvc.perform(
//        post(PostWebAdapter.POST_API_URI)
//            .contentType(APPLICATION_JSON)
//            .content(toJson(command)));
//
//    when.andExpectAll(status().isBadRequest());
//    then(postCrudRepository).should(never()).save(any());
//  }
//
//  @Test
//  void 게시글_조회() throws Exception {
//    final var postId = 1L;
//    final var information = PostFactory.createInfo(postId);
//    given(postQueryRepository.findInfoById(postId)).willReturn(Optional.of(information));
//
//    final var when = mockMvc.perform(
//        get(PostWebAdapter.POST_API_URI + "/{id}", postId));
//
//    when.andExpectAll(
//        status().isOk(),
//        content().json(toJson(information)));
//  }
//
//  @Test
//  void 존재하지_않는_게시글_조회() throws Exception {
//    final var id = 1L;
//    final var error = new ErrorResponse(Error.POST_NOT_FOUND.message());
//    given(postQueryRepository.findInfoById(id)).willReturn(Optional.empty());
//
//    final var when = mockMvc.perform(
//        get(PostWebAdapter.POST_API_URI + "/{id}", id));
//
//    when.andExpectAll(
//        status().isNotFound(),
//        content().json(toJson(error)));
//  }
//
//  @WithUser1
//  @Test
//  void 게시글_업데이트() throws Exception {
//    final var id = 1L;
//    final var command = new Update("newTitle", "newContent");
//    final var post1 = PostFactory.createPost1(UserFactory.createUser1());
//    given(postCrudRepository.findById(id)).willReturn(Optional.of(post1));
//
//    final var when = mockMvc.perform(
//        patch(PostWebAdapter.POST_API_URI + "/{id}", id)
//            .contentType(APPLICATION_JSON)
//            .content(toJson(command)));
//
//    when.andExpectAll(status().isNoContent());
//  }
//
//  @WithUser1
//  @NullAndEmptySource
//  @ParameterizedTest
//  void 비어있는_제목으로_게시글_업데이트(final String title) throws Exception {
//    final var id = 1L;
//    final var post1 = PostFactory.createPost1(UserFactory.createUser1());
//    final var command = new Update(title, "newContent");
//    given(postCrudRepository.findById(id)).willReturn(Optional.of(post1));
//
//    final var when = mockMvc.perform(
//        patch(PostWebAdapter.POST_API_URI + "/{id}", id)
//            .contentType(APPLICATION_JSON)
//            .content(toJson(command)));
//
//    when.andExpectAll(status().isNoContent());
//    assertThat(post1.title()).isNotEqualTo(title);
//    assertThat(post1.content()).isEqualTo(command.content());
//  }
//
//  @WithUser1
//  @Test
//  void 존재하지_않는_게시글_업데이트() throws Exception {
//    final var id = 1L;
//    final var command = new Update("newTitle", "newContent");
//    final var error = new ErrorResponse(Error.POST_NOT_FOUND.message());
//    given(postCrudRepository.findById(id)).willReturn(Optional.empty());
//
//    final var when = mockMvc.perform(
//        patch(PostWebAdapter.POST_API_URI + "/{id}", id)
//            .contentType(APPLICATION_JSON)
//            .content(toJson(command)));
//
//    when.andExpectAll(
//        status().isNotFound(),
//        content().json(toJson(error)));
//  }
//
//  @WithUser1
//  @Test
//  void 다른_사람의_게시글_업데이트() throws Exception {
//    final var id = 1L;
//    final var command = new Update("newTitle", "newContent");
//    final var user2 = UserFactory.createUser2();
//    final var post1 = PostFactory.createPost1(user2);
//    final var error = new ErrorResponse(Error.FORBIDDEN.message());
//    given(postCrudRepository.findById(id)).willReturn(Optional.of(post1));
//
//    final var when = mockMvc.perform(
//        patch(PostWebAdapter.POST_API_URI + "/{id}", id)
//            .contentType(APPLICATION_JSON)
//            .content(toJson(command)));
//
//    when.andExpectAll(
//        status().isForbidden(),
//        content().json(toJson(error)));
//  }
//
//  @WithUser1
//  @Test
//  void 게시글_삭제() throws Exception {
//    final var id = 1L;
//    final var post1 = PostFactory.createPost1(UserFactory.createUser1());
//    given(postCrudRepository.findById(id)).willReturn(Optional.of(post1));
//
//    final var when = mockMvc.perform(
//        delete(PostWebAdapter.POST_API_URI + "/{id}", id));
//
//    when.andExpectAll(status().isNoContent());
//    then(postCrudRepository).should().delete(any());
//  }
//
//  @WithUser1
//  @Test
//  void 존재하지_않는_게시글_삭제() throws Exception {
//    final var id = 1L;
//    final var error = new ErrorResponse(Error.POST_NOT_FOUND.message());
//    given(postCrudRepository.findById(id)).willReturn(Optional.empty());
//
//    final var when = mockMvc.perform(
//        delete(PostWebAdapter.POST_API_URI + "/{id}", id));
//
//    when.andExpectAll(
//        status().isNotFound(),
//        content().json(toJson(error)));
//    then(postCrudRepository).should(never()).delete(any());
//  }
//
//  @WithUser1
//  @Test
//  void 다른_사람의_게시글_삭제() throws Exception {
//    final var id = 1L;
//    final var user2 = UserFactory.createUser2();
//    final var post1 = PostFactory.createPost1(user2);
//    final var error = new ErrorResponse(Error.FORBIDDEN.message());
//    given(postCrudRepository.findById(id)).willReturn(Optional.of(post1));
//
//    final var when = mockMvc.perform(
//        delete(PostWebAdapter.POST_API_URI + "/{id}", id));
//
//    when.andExpectAll(
//        status().isForbidden(),
//        content().json(toJson(error)));
//    then(postCrudRepository).should(never()).delete(any());
//  }
//
//}
