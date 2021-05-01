package com.seneca.senecaforum.repository;

import com.seneca.senecaforum.domain.Comment;
import com.seneca.senecaforum.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
}
