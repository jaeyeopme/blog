package me.jaeyeop.blog.post.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import java.util.Optional;
import me.jaeyeop.blog.config.error.exception.PostNotFoundException;
import me.jaeyeop.blog.post.adapter.in.PostRequest.Find;
import me.jaeyeop.blog.support.UnitTestSupport;
import me.jaeyeop.blog.support.helper.PostHelper;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;

/**
 * @author jaeyeopme Created on 10/12/2022.
 */
class PostQueryServiceTest extends UnitTestSupport {

  @Test
  void 게시글_조회() {
    final var postId = 1L;
    final var info = PostHelper.createInfo(postId);
    given(postQueryPort.findInfoById(postId)).willReturn(Optional.of(info));

    final var actual = postQueryService.findOne(new Find(postId));

    assertThat(actual).isEqualTo(info);
  }

  @Test
  void 존재하지_않는_게시글_조회() {
    final var postId = 1L;
    given(postQueryPort.findInfoById(postId)).willReturn(Optional.empty());

    final ThrowingCallable when = () -> postQueryService.findOne(new Find(postId));

    assertThatThrownBy(when).isInstanceOf(PostNotFoundException.class);
  }

}
