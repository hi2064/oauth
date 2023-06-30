package com.ordo.oauth.service;

import com.ordo.oauth.domain.dto.CommentDto;
import com.ordo.oauth.domain.dto.CommentV2;
import com.ordo.oauth.domain.dto.PostCommentRequest;
import com.ordo.oauth.domain.dto.PostDto;
import com.ordo.oauth.domain.dto.PostDtoV2;
import com.ordo.oauth.domain.dto.PostRequest;
import com.ordo.oauth.domain.dto.PostRequestV2;
import com.ordo.oauth.domain.entity.CommentEntity;
import com.ordo.oauth.domain.entity.PostEntity;
import com.ordo.oauth.domain.entity.PostEntityV2;
import com.ordo.oauth.domain.response.CommentResponse;
import com.ordo.oauth.model.ApiResult;
import java.util.List;
import java.util.Optional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;


public interface PostServiceV2 {

  List<PostDtoV2> posts();

  Long getListCount();

  PostDtoV2 getPost(Integer id);

  Integer getLikeCount(Integer postId, Authentication auth);

  Integer create(PostRequest request, Authentication auth);

  Integer modify(PostRequestV2 request, Authentication auth);

  Integer delete(Integer postId, Authentication auth);

  String likePost(Integer postId, Authentication auth);

  String likeCancelPost(Integer postId, Authentication auth);

  Optional<List<CommentDto>> getComment(Integer postId);

  Integer modifyComment(Integer commentId, CommentV2 dto, Authentication auth);

  Integer createComment(Integer postId, PostCommentRequest request, Authentication auth);

  Integer deleteComment(Integer commentId, Authentication auth);

}
