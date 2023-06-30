package com.ordo.oauth.repository;

import com.ordo.oauth.domain.dto.CommentDto;
import com.ordo.oauth.domain.entity.CommentEntity;
import com.ordo.oauth.domain.entity.CommentEntityV2;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentEntityRepositoryV2 extends JpaRepository<CommentEntityV2, Integer> {

  Optional<List<CommentDto>> findByPostId(Integer id);

  void deleteAllByPostId(Integer postId);

//  void deleteAllByPostId(Integer postId);
}
