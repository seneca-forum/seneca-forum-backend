package com.seneca.senecaforum.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "posts")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Integer postId;

    @Column(name = "title",nullable = false)
    private String title;

    @Column(name = "content",nullable = false)
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
    @JsonIgnore
    private Topic topic;

    @OneToMany(
            mappedBy = "post",
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER
    )
//    private Set<Comment> comments = new TreeSet<>();
    private List<Comment>comments;

    @Column(name="post_tags")
    private String tags;

    @Column(name = "views",nullable = false)
    private Integer views;

    public void addComment(Comment p){
        this.comments.add(p);
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
