package com.ordo.oauth.repository;

import com.ordo.oauth.domain.entity.LikeEntityV2;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeEntityRepositoryV2 extends JpaRepository<LikeEntityV2, Integer> {

  Integer countByPostId(Integer postId);

  void deleteByPostIdAndUserId(Integer postId, Integer userId);

  void deleteAllByPostId(Integer postId);

  Integer countByUserIdAndPostId(Integer userId, Integer postid);

  LikeEntityV2 findByPostIdAndUserId(Integer postId, Integer userId);
}
