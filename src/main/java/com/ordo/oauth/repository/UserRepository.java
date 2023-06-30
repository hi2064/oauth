package com.ordo.oauth.repository;

import com.ordo.oauth.domain.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    Optional<UserEntity> findByUserName(String userName);

    UserEntity findAllByUserName(String userName);

    Long countByUserName(String userName);
}
