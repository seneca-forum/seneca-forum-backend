package com.seneca.senecaforum.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.OrderBy;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

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

    @Column(name = "title")
    private String title;

    @Column(name = "createdOn")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdOn;

    @Column(name = "noOfReplies")
    @OrderBy(clause = "desc")
    private Integer noOfReplies;

    @OneToOne
    @JoinColumn(name = "author_id")
    private User user;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "topic_id")
    @JsonIgnore
    private Topic topic;

    @OneToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @JoinColumn(name = "post_id")
    private List<Comment> comments;

}
