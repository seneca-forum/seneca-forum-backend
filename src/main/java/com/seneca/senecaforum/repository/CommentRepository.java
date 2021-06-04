package com.seneca.senecaforum.repository;

import com.seneca.senecaforum.domain.Comment;
import com.seneca.senecaforum.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository  extends JpaRepository<Comment,Integer>{
    @Query(value ="select * from comments where post_id =:postId",
            nativeQuery = true)
    Page<Comment> findAllByPostId(int postId, Pageable pageable);

    @Query(value = "select count(*) from posts left outer join comments on comments.post_id = posts.post_id \n" +
            "where comments.comment_id is not null and posts.topic_id=:topicId",
            nativeQuery = true)
    int getNoOfCommentsByTopicId (String topicId);
}
