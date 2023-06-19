package com.ordo.oauth.repository;

import com.ordo.oauth.domain.entity.PostEntity;
import com.ordo.oauth.domain.entity.PostEntityV2;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepositoryV2  extends JpaRepository<PostEntityV2, Integer> {


  Optional<PostEntityV2> findById(Integer id);

  Optional<PostEntityV2> findByIdAndAndUserId(Integer id, Integer userId);
}
