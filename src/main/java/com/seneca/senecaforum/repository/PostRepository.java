package com.seneca.senecaforum.repository;

import com.seneca.senecaforum.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {
    @Query("FROM Post p WHERE p.topic.topicId = :topicId")
    List<Post> findAllByTopicId(int topicId);
}
