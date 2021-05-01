package com.seneca.senecaforum.repository;

import com.seneca.senecaforum.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
//
//    @Query("FROM User u WHERE u.userId = :userId")
//    Optional<User> findByUserId(String userId);
}
