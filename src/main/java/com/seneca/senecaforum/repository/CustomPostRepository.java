package com.seneca.senecaforum.repository;

import com.seneca.senecaforum.domain.Post;
import com.seneca.senecaforum.domain.Topic;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomPostRepository {

    List<Post> filterPostsBasedOnKeywords(String topicID, String tags, String start, String end, String sortBy, String order, Pageable pageable);

    List<Post> findPostsContainKeywords(String keyword);

    List<Post> getAllPostsOrderByPending();

    List<Post> getAllPostsByUserIdOrderByPending(String userId);
}
