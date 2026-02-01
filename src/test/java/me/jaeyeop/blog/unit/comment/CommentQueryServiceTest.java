package me.jaeyeop.blog.unit.comment;

import java.util.Optional;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;

import me.jaeyeop.blog.comment.application.port.in.CommentQueryUseCase.PageQuery;
import me.jaeyeop.blog.comment.application.port.in.CommentQueryUseCase.Query;
import me.jaeyeop.blog.comment.application.port.out.CommentQueryPort;
import me.jaeyeop.blog.comment.application.service.CommentQueryService;
import me.jaeyeop.blog.commons.error.exception.CommentNotFoundException;
import me.jaeyeop.blog.commons.error.exception.PostNotFoundException;
import me.jaeyeop.blog.post.application.port.out.PostQueryPort;
import me.jaeyeop.blog.support.UnitTest;
import me.jaeyeop.blog.support.factory.CommentFactory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

class CommentQueryServiceTest extends UnitTest {
    @InjectMocks
    private CommentQueryService commentQueryService;

    @Mock(stubOnly = true)
    private CommentQueryPort commentQueryPort;

    @Mock(stubOnly = true)
    private PostQueryPort postQueryPort;

    @Test
    void 댓글_조회() {
        // GIVEN
        final var stub = CommentFactory.createInformation(55L);
        given(commentQueryPort.findInformationById(stub.id())).willReturn(Optional.of(stub));
        final var query = new Query(stub.id());

        // WHEN
        final var information = commentQueryService.findInformationById(query);

        // THEN
        assertThat(information).isEqualTo(stub);
    }

    @Test
    void 존재하지_않은_댓글_조회() {
        // GIVEN
        final var commentId = 11L;
        given(commentQueryPort.findInformationById(commentId)).willReturn(Optional.empty());
        final var query = new Query(commentId);

        // WHEN
        final ThrowingCallable when = () -> commentQueryService.findInformationById(query);

        // THEN
        assertThatThrownBy(when).isInstanceOf(CommentNotFoundException.class);
    }

    @Test
    void 댓글_페이지_조회() {
        // GIVEN
        final var postId = 1L;
        final var pageable = getPageable();
        final var infoPage = CommentFactory.createInformationPage(pageable);
        given(postQueryPort.existsById(postId)).willReturn(Boolean.TRUE);
        given(commentQueryPort.findInformationPageByPostId(postId, pageable)).willReturn(infoPage);
        final var query = new PageQuery(postId, pageable);

        // WHEN
        final var actual = commentQueryService.findInformationPageByPostId(query);

        // THEN
        assertThat(actual).isEqualTo(infoPage);
    }

    @Test
    void 존재하지_않은_게시글의_댓글_페이지_조회() {
        // GIVEN
        final var postId = 1L;
        final var pageable = getPageable();
        given(postQueryPort.existsById(postId)).willReturn(Boolean.FALSE);
        final var query = new PageQuery(postId, pageable);

        // WHEN
        final ThrowingCallable when = () -> commentQueryService.findInformationPageByPostId(query);

        // THEN
        assertThatThrownBy(when).isInstanceOf(PostNotFoundException.class);
    }

    private PageRequest getPageable() {
        return PageRequest.of(5, 10, Direction.DESC, "createdAt");
    }
}
