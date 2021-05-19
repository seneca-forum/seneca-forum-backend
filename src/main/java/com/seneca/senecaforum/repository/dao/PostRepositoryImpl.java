package com.seneca.senecaforum.repository.dao;

import com.seneca.senecaforum.domain.Comment;
import com.seneca.senecaforum.domain.Post;
import com.seneca.senecaforum.domain.Topic;
import com.seneca.senecaforum.repository.CustomPostRepository;
import org.hibernate.Criteria;
import org.hibernate.type.ShortType;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
    public List<Post> findPostsByTopicDefault(
            Topic topic, String methodOrder, Date start,Date end, String tags, Pageable pageable) {
        methodOrder = Objects.isNull(methodOrder) ? "DESC" : "ASC";
        StringBuilder sql = new StringBuilder();
//        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
//        CriteriaQuery<Post> q = cb.createQuery(Post.class);
//        Root<Post> posts = q.from(Post.class);
//        Join<Post, Comment> comments = posts.join("comments", JoinType.LEFT);
//
        sql.append("SELECT * FROM POSTS p LEFT JOIN(SELECT c.post_id AS belongPost,MAX(created_on)AS LASTCOMMENT FROM COMMENTS c GROUP BY belongPost) AS TEMP ");
        sql.append("ON TEMP.belongPost = p.post_id ");
        sql.append("WHERE p.topic_id = :topicId ");
        sql.append("AND p.post_tags LIKE :tags ");
        sql.append("ORDER BY TEMP.LASTCOMMENT ").append(methodOrder);
        sql.append(",IF(TEMP.LASTCOMMENT IS NULL, 1, 0),p.created_on ").append(methodOrder);
        int pageNumber = pageable.getPageNumber();
        int pageSize = pageable.getPageSize();
        Query q = entityManager.createNativeQuery(sql.toString(),Post.class);
        q.setParameter("topicId",topic.getTopicId());
        q.setParameter("tags",Objects.isNull(tags) ? "%" : "%"+tags+"%");
        q.setFirstResult(pageNumber * pageSize);
        q.setMaxResults(pageSize);
        List<Post> a = q.getResultList();
        String b = "c";
        return q.getResultList();
    }
}
