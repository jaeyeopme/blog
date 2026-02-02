package me.jaeyeop.blog.unit.post;

import java.util.Optional;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import me.jaeyeop.blog.commons.error.exception.PostNotFoundException;
import me.jaeyeop.blog.post.application.port.in.PostQueryUseCase.InformationQuery;
import me.jaeyeop.blog.post.application.port.out.PostQueryPort;
import me.jaeyeop.blog.post.application.service.PostQueryService;
import me.jaeyeop.blog.support.UnitTest;
import me.jaeyeop.blog.support.factory.PostFactory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

class PostQueryServiceTest extends UnitTest {
    @InjectMocks
    private PostQueryService postQueryService;

    @Mock(stubOnly = true)
    private PostQueryPort postQueryPort;

    @Test
    @DisplayName("Find information by id")
    void find_information_by_id() {
        final var postId = 1L;
        final var information = PostFactory.createInformation(postId);
        given(postQueryPort.findInformationById(postId)).willReturn(Optional.of(information));
        final var query = new InformationQuery(postId);

        final var foundInformation = postQueryService.findInformationById(query);

        assertThat(foundInformation).isEqualTo(information);
    }

    @Test
    @DisplayName("Find information by id not found")
    void find_information_by_id_not_found() {
        final var postId = 1L;
        given(postQueryPort.findInformationById(postId)).willReturn(Optional.empty());
        final var query = new InformationQuery(postId);

        final ThrowingCallable when = () -> postQueryService.findInformationById(query);

        assertThatThrownBy(when).isInstanceOf(PostNotFoundException.class);
    }
}
