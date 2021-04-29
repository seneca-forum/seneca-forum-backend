package com.seneca.senecaforum.repository;

import com.seneca.senecaforum.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Integer> {
}
