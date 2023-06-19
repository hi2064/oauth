package com.ordo.oauth.repository;

import com.ordo.oauth.domain.entity.PostEntity;
import com.ordo.oauth.domain.entity.UserEntity;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<PostEntity, Integer> {
//    Optional<PostEntity> findByUserid(Integer userid);
    Page<PostEntity> findAllByUser(UserEntity userEntity, Pageable pageable);



}
