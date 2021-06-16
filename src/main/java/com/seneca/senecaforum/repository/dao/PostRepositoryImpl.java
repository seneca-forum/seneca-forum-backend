package com.seneca.senecaforum.repository.dao;

import com.seneca.senecaforum.domain.Post;
import com.seneca.senecaforum.domain.Topic;
import com.seneca.senecaforum.repository.CustomPostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;
import java.util.Objects;

@Repository
public class PostRepositoryImpl implements CustomPostRepository {

    private final EntityManager entityManager;

    @Autowired
    public PostRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager.getEntityManagerFactory().createEntityManager();
    }
    @Override
    public List<Post> findPostsByTopicBasedOnComment(
            Topic topic, String methodOrder, String tags, Pageable pageable) {
        methodOrder = (Objects.isNull(methodOrder) || methodOrder.equals("desc")) ? "DESC" : "ASC";
        StringBuilder sql = new StringBuilder();
        if (methodOrder.equals("ASC"))
            sql.append("SELECT * FROM POSTS p LEFT JOIN(SELECT c.post_id AS belongPost,MIN(created_on)AS COMMENT FROM COMMENTS c GROUP BY belongPost) AS TEMP ");
        else
            sql.append("SELECT * FROM POSTS p LEFT JOIN(SELECT c.post_id AS belongPost,MAX(created_on)AS COMMENT FROM COMMENTS c GROUP BY belongPost) AS TEMP ");
        sql.append("ON TEMP.belongPost = p.post_id ")
            .append("WHERE p.topic_id = :topicId ")
            .append("AND p.status = 'ACCEPTED'")
            .append("AND p.post_tags LIKE :tags ")
            .append("ORDER BY IF(TEMP.COMMENT IS NULL, 1, 0),TEMP.COMMENT ").append(methodOrder)
            .append(",p.created_on ").append(methodOrder);
        int pageNumber = pageable.getPageNumber();
        int pageSize = pageable.getPageSize();
        Query q = entityManager.createNativeQuery(sql.toString(),Post.class);
        q.setParameter("topicId",topic.getTopicId());
        q.setParameter("tags",Objects.isNull(tags) ? "%%" : "%"+tags+"%");
        q.setFirstResult(pageNumber * pageSize);
        q.setMaxResults(pageSize);
        return q.getResultList();
    }

    @Override
    public List<Post> findPostsContainKeywords(String keyword) {
        String[] tokens = keyword.split(" ");
        String query = "select * from posts p where p.content like";
        for(int i = 0; i < tokens.length; i++){
            if(i!=0){
                query+= " or ";
            }
            query+= " '%"+tokens[i].toString()+"%' ";
        }
        Query q = entityManager.createNativeQuery(query.toString(),Post.class);

        return q.getResultList();
    }

    @Override
    public List<Post> getAllPostsOrderByPending() {
        String sql = "SELECT * FROM POSTS p " +
                "ORDER BY IF(status='PENDING',0,if(status='ACCEPTED',1,2)),p.created_on ASC";
        Query q = entityManager.createNativeQuery(sql,Post.class);
        return q.getResultList();
    }

    @Override
    public List<Post> getAllPostsByUserIdOrderByPending(String userId) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM POSTS p WHERE p.author_id = :userId ")
                .append("ORDER BY IF(status='PENDING',0,if(status='ACCEPTED',1,2)),p.created_on ASC");
        Query q = entityManager.createNativeQuery(sql.toString(),Post.class);
        q.setParameter("userId",userId);
        return q.getResultList();
    }

}
