package com.seneca.senecaforum.repository.dao;

import com.seneca.senecaforum.domain.Post;
import com.seneca.senecaforum.domain.Topic;
import com.seneca.senecaforum.repository.CustomPostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.Date;
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
            .append("")
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

//    @Override
//    public List<PostViewDto> getHotPosts() {
//        StringBuilder sql = new StringBuilder();
//        sql.append("select * from posts left outer join\n" +
//                "(select comments.post_id, count(*)as totalCmts from comments group by comments.post_id)\n" +
//                "as tempC on posts.post_id = tempC.post_id\n" +
//                "order by posts.views desc, tempC.totalCmts desc;\n");
//        Query q = entityManager.createNativeQuery(sql.toString(),Post.class)
//                .unwrap(org.hibernate.query.Query.class)
//                .setResultTransformer(Transformers.aliasToBean(PostVie.class));
//        return null;
//    }

}
