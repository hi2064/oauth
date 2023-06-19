package com.ordo.oauth.service;

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

  PostDtoV2 getPost(Integer id);

  Integer create(PostRequest request, Authentication auth);

  Integer modify(Integer userId, PostRequestV2 request);

  Integer delete(Integer postId, Authentication auth);

  Integer modifyComment(Integer commentId, CommentV2 dto);

  Integer createComment(Integer postId, PostCommentRequest request, Authentication auth);

  Integer deleteComment(Integer commentId);

}
