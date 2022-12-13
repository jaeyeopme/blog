package me.jaeyeop.blog.post.adapter.in;

import static me.jaeyeop.blog.post.adapter.in.PostRequest.Create;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import me.jaeyeop.blog.config.oas.dto.OASResponse.InvalidArgumentResponse;
import me.jaeyeop.blog.config.oas.dto.OASResponse.NotFoundPostResponse;
import me.jaeyeop.blog.config.oas.dto.OASResponse.SecurityResponse;
import me.jaeyeop.blog.config.security.authentication.UserPrincipal;
import me.jaeyeop.blog.post.adapter.in.PostRequest.Update;
import me.jaeyeop.blog.post.adapter.out.PostResponse.Info;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;

@Validated
@Tag(name = "3. post", description = "게시글(게시글 작성, 게시글 조회, 게시글 수정, 게시글 삭제)")
public interface PostOAS {

  @NotFoundPostResponse
  @SecurityResponse
  @ApiResponse(responseCode = "204", description = "자신의 게시글 삭제 성공")
  @Operation(summary = "Delete my post by post targetId", description = "자신의 게시글을 삭제합니다.")
  void delete(
      UserPrincipal principal,
      @Schema(description = "자신의 게시글 식별자") Long postId);

  @NotFoundPostResponse
  @ApiResponse(responseCode = "200", description = "게시글 조회 성공",
      content = @Content(schema = @Schema(implementation = Info.class)))
  @Operation(summary = "Find one post by post targetId", description = "게시글을 조회합니다.")
  Info findOne(@Schema(description = "게시글 식별자") Long postId);

  @InvalidArgumentResponse
  @NotFoundPostResponse
  @SecurityResponse
  @ApiResponse(responseCode = "204", description = "게시글 수정 성공")
  @Operation(summary = "Update my post by post targetId", description = "자신의 게시글을 수정합니다.")
  void update(
      UserPrincipal principal,
      @Schema(description = "자신의 게시글 식별자") Long postId, Update request);

  @InvalidArgumentResponse
  @SecurityResponse
  @ApiResponse(responseCode = "201", description = "게시글 작성 성공",
      headers = @Header(name = HttpHeaders.LOCATION, description = "게시글 조회 URI", required = true))
  @Operation(summary = "Create my post", description = "게시글을 작성합니다.")
  ResponseEntity<Void> create(UserPrincipal principal, @Valid Create request);

}
