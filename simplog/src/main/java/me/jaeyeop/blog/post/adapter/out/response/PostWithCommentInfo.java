package me.jaeyeop.blog.post.adapter.out.response;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import me.jaeyeop.blog.comment.adapter.out.response.CommentInfo;
import org.springframework.data.domain.Page;

@Getter
@EqualsAndHashCode
public class PostWithCommentInfo {

  private final PostInfo postInfo;

  private final Page<CommentInfo> commentInfos;

  public PostWithCommentInfo(final PostInfo postInfo,
      final Page<CommentInfo> commentInfos) {
    this.postInfo = postInfo;
    this.commentInfos = commentInfos;
  }

}
