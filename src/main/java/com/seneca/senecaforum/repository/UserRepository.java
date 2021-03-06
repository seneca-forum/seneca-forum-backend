package com.seneca.senecaforum.repository;

import com.seneca.senecaforum.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity,String> {

    Optional<UserEntity> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByUserId(String userId);

    @Query("FROM UserEntity u WHERE u.email = :email")
    Optional<UserEntity> findOneWithAuthoritiesByEmail(String email);

}
