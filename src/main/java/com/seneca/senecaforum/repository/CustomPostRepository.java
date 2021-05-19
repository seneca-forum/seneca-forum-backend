package com.seneca.senecaforum.repository;

import com.seneca.senecaforum.domain.Post;
import com.seneca.senecaforum.domain.Topic;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;

public interface CustomPostRepository {

    List<Post> findPostsByTopicDefault(Topic topic, String methodOder,Date start, Date end, String tags, Pageable pageable);
}
