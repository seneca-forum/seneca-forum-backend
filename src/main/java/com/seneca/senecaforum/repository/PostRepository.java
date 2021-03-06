package com.seneca.senecaforum.repository;

import com.seneca.senecaforum.domain.Post;
import com.seneca.senecaforum.domain.Topic;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post,Integer>,CustomPostRepository{

    List<Post> findPostsByOrderByCreatedOnDesc();

    @Query(value = "SELECT * FROM posts p LEFT JOIN(SELECT c.post_id AS belongPost,MAX(created_on)AS COMMENT FROM comments c GROUP BY belongPost) AS TEMP \n" +
            "ON TEMP.belongPost = p.post_id WHERE p.topic_id =:topicId\n" +
            "AND p.status = 'ACCEPTED'\n" +
            "ORDER BY IF(TEMP.COMMENT IS NULL, 1, 0),TEMP.COMMENT DESC,created_on desc", nativeQuery = true)
    List<Post> findPostsByTopicIdSortedByLatestComment(String topicId, Pageable pageable);

    @Query("FROM Post p WHERE p.topic =:topic " +
            "AND (:startDate IS NULL OR p.createdOn >= :startDate) " +
            "AND (:endDate IS NULL OR p.createdOn <= :endDate) " +
            "AND (:tags IS NULL OR p.tags LIKE %:tags%) " +
            "AND (p.status = 'ACCEPTED') ")
    List<Post> findPostsByTopicBasedOnPost(Topic topic, String tags, Date startDate, Date endDate, Pageable pageable);

    @Query(value="select * from posts left outer join\n" +
            "(select comments.post_id, count(*)as noOfComments from comments group by comments.post_id)\n" +
            "as tempC on posts.post_id = tempC.post_id where posts.status = 'ACCEPTED'\n" +
            "order by posts.views desc, tempC.noOfComments desc/*:pageable*/",
            nativeQuery = true)
    List<Post>getHotPosts(Pageable pageable);

    @Query(value = "select count(*) from posts where topic_id =:topicId",
            nativeQuery = true)
    Integer getNoOfPostsByTopicId (String topicId);

    @Query("FROM Post p where p.author.userId =:userId")
    List<Post>getAllPostsByUserId(String userId);

    @Query("SELECT COUNT(*) FROM Post p where p.status =:status")
    Integer countByStatusEquals(String status);

    @Query("SELECT SUM(p.views) FROM Post p WHERE p.topic.topicId =:topicId group by p.topic.topicId")
    Integer getNoOfViewsByTopicId(String topicId);

}
