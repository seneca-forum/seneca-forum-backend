package com.seneca.senecaforum.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Integer commentId;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "created_on", nullable = false)
    private Date createdOn;

    @ManyToOne
    @JoinColumn(
            name = "commenter_id",
            nullable = false,
            referencedColumnName = "user_id",
            foreignKey = @ForeignKey(name = "FK_COMMENTER_COMMENT")
    )
    private User commenter;
//
//    @ManyToOne
//    @JoinColumn(
//            name="post_id",
//            nullable = false,
//            foreignKey=@ForeignKey(name = "FK_POST_COMMENT")
//    )
//    @JsonIgnore
//    private Post post;


}

