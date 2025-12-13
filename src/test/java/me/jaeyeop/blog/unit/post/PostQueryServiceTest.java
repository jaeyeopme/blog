package me.jaeyeop.blog.unit.post;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import java.util.Optional;
import me.jaeyeop.blog.commons.error.exception.PostNotFoundException;
import me.jaeyeop.blog.post.application.port.in.PostQueryUseCase.InformationQuery;
import me.jaeyeop.blog.support.UnitTest;
import me.jaeyeop.blog.support.helper.PostHelper;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;

/**
 * @author jaeyeopme Created on 10/12/2022.
 */
class PostQueryServiceTest extends UnitTest {

    @Test
    void 게시글_조회() {
        // GIVEN
        final var postId = 1L;
        final var information = PostHelper.createInformation(postId);
        given(postQueryPort.findInformationById(postId)).willReturn(Optional.of(information));
        final var query = new InformationQuery(postId);

        // WHEN
        final var foundInformation = postQueryService.findInformationById(query);

        // THEN
        assertThat(foundInformation).isEqualTo(information);
    }

    @Test
    void 존재하지_않는_게시글_조회() {
        // GIVEN
        final var postId = 1L;
        given(postQueryPort.findInformationById(postId)).willReturn(Optional.empty());
        final var query = new InformationQuery(postId);

        // WHEN
        final ThrowingCallable when = () -> postQueryService.findInformationById(query);

        // THEN
        assertThatThrownBy(when).isInstanceOf(PostNotFoundException.class);
    }
}
