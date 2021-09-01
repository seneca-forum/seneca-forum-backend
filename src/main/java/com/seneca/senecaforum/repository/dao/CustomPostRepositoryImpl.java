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
public class CustomPostRepositoryImpl implements CustomPostRepository {

    private final EntityManager entityManager;

    @Autowired
    public CustomPostRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager.getEntityManagerFactory().createEntityManager();
    }
    @Override
    public List<Post> filterPostsBasedOnKeywords(
            String topicID, String tags, String start, String end, String sortBy, String order, Pageable pageable) {
        String MINMAX = (order.equals("ASC") ? "MIN" : "MAX");
        String tagStr = (tags==null?"\"%%\"":("\"%"+tags+"%\""));
        end = end + "  23:59:59"; // get until the end of the day
        StringBuilder sb = new StringBuilder();

        sb.append("SELECT * FROM posts p LEFT JOIN(SELECT c.post_id AS belongPost,").append(MINMAX)
                .append("(created_on)AS COMMENT FROM comments c GROUP BY belongPost) AS TEMP ")
                .append("ON TEMP.belongPost = p.post_id ")
                .append("WHERE p.topic_id = '").append(topicID).append("'")
                .append(" AND p.status = 'ACCEPTED'")
                .append(" AND p.post_tags LIKE ").append(tagStr)
                .append(" AND p.created_on >= \"").append(start).append("\"")
                .append(" AND p.created_on <= \"").append(end).append("\"")
                .append(" ORDER BY IF(TEMP.COMMENT IS NULL, 1, 0),TEMP.COMMENT ").append(order)
                .append(",p.created_on ").append(order);

        int pageNumber = pageable.getPageNumber();
        int pageSize = pageable.getPageSize();
        Query q = entityManager.createNativeQuery(sb.toString(),Post.class);
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
        String sql = "SELECT * FROM posts p " +
                "ORDER BY IF(status='PENDING',0,if(status='ACCEPTED',1,2)),p.created_on ASC";
        Query q = entityManager.createNativeQuery(sql,Post.class);
        return q.getResultList();
    }

    @Override
    public List<Post> getAllPostsByUserIdOrderByPending(String userId) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM posts p WHERE p.author_id = :userId ")
                .append("ORDER BY IF(status='PENDING',0,if(status='ACCEPTED',1,2)),p.created_on ASC");
        Query q = entityManager.createNativeQuery(sql.toString(),Post.class);
        q.setParameter("userId",userId);
        return q.getResultList();
    }

}
