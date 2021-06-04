package com.seneca.senecaforum.repository;


import com.seneca.senecaforum.domain.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TopicRepository extends JpaRepository<Topic,String> {
    @Query("SELECT t FROM Topic t ORDER BY t.views DESC")
    List<Topic> findAll();

    @Query(value="select * from topics left outer join \n" +
            "(select posts.topic_id, count(*) as totalPosts from posts  where posts.topic_id is not null group by topic_id) \n" +
            "as tempP on topics.topic_id = tempP.topic_id \n" +
            "left outer join\n" +
            "(select posts.topic_id, count(*) as totalCmts from posts left outer join comments on comments.post_id = posts.post_id \n" +
            "where comments.comment_id is not null \n" +
            "group by posts.topic_id)\n" +
            "as tempC on topics.topic_id = tempC.topic_id\n" +
            "order by tempP.totalPosts desc,tempC.totalCmts desc,topics.views desc;", nativeQuery = true)
    List<Topic>getSortedTopicsByNoOfPostsCmts();

}
