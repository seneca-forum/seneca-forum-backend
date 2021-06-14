package com.seneca.senecaforum.repository;

import com.seneca.senecaforum.domain.Post;
import com.seneca.senecaforum.domain.Topic;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomPostRepository {

    List<Post> findPostsByTopicBasedOnComment(Topic topic, String methodOrder, String tags, Pageable pageable);

    List<Post> findPostsContainKeywords(String keyword);

    List<Post> getAllPostsOrderByPending();
}
