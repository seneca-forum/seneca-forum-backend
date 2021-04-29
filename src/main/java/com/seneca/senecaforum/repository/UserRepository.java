package com.seneca.senecaforum.repository;

import com.seneca.senecaforum.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {

    @Query("FROM User u WHERE u.userId = :userId")
    Optional<User> findByUserId(String userId);
}
