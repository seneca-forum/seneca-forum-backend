package com.seneca.senecaforum.domain;


import org.hibernate.annotations.OrderBy;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "posts")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Post{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Integer postId;

    @Column(name = "title",nullable = false)
    private String title;

    @Column(name = "content",nullable = false,length = 8000)
    private String content;

    @Column(name = "created_on",nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdOn;

    @Column(name = "edited_on")
    @Temporal(TemporalType.TIMESTAMP)
    private Date editedOn;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(
            name="author_id",
            nullable = false,
            referencedColumnName="user_id",
            foreignKey=@ForeignKey(name = "FK_AUTHOR_POST")
    )
    private User author;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(
            name = "topic_id",
            nullable = false,
            referencedColumnName="topic_id",
            foreignKey=@ForeignKey(name = "FK_TOPIC_POST")
    )
    private Topic topic;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name="post_id")
    @OrderBy(clause = "createdOn")
    private List<Comment> comments;

    @Column(name="post_tags")
    private String tags;

    @Column(name = "views",nullable = false)
    private Integer views;

    public void addComment(Comment comment){
        if(comments == null)
            comments = new ArrayList<>();
        this.comments.add(comment);
    }

    @Override
    public int hashCode() {
        int prime = 31;
        return prime+ ((postId==null)?0:prime+postId.hashCode());
    }

    @Override
    public boolean equals(Object obj) {
        if(obj==null){
            return false;
        }
        if(postId.getClass()==obj.getClass()){
            return true;
        }
        Post post = (Post) obj;
        if(this.postId==null&&post.getPostId()!=null){
            return false;
        }
        else if(this.postId!=null && !this.postId.equals(post.getPostId())){
            return false;
        }
        return true;
    }

}
