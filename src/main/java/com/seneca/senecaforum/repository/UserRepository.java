package com.seneca.senecaforum.repository;

import com.seneca.senecaforum.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,String> {

    @Query("FROM User u WHERE u.username = :username")
    Optional<User> findByUsername(String username);

    @Query("FROM User u WHERE u.email = :email")
    Optional<User> findByEmail(String email);
}
