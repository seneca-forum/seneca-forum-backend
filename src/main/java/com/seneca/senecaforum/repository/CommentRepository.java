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

    @Query(value = "select count(*) from comments where post_id =:postId",
    nativeQuery = true)
    int getNoOfComments (int postId);
}
