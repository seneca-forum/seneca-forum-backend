package com.seneca.senecaforum.repository;

import com.seneca.senecaforum.domain.Post;
import com.seneca.senecaforum.domain.Topic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {

    Page<Post> findDistinctByTopic(Topic topic,Pageable pageable);

    @Query("FROM Post p WHERE p.topic =:topic " +
            "AND (:startDate is null or p.createdOn >= :startDate) " +
            "AND (:endDate is null or p.createdOn <= :endDate) " +
            "AND (:tags is null or p.tags LIKE %:tags%) ")
    Page<Post> findByFilterDateAndTags(Topic topic,String tags, Date startDate, Date endDate,Pageable pageable);

}
