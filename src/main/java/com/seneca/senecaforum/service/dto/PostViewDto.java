package com.seneca.senecaforum.service.dto;

import com.seneca.senecaforum.domain.Comment;
import com.seneca.senecaforum.domain.Topic;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostViewDto {
    private Integer postId;
    private String content;
    private Date createdOn;
    private String tags;
    private String title;
    private Integer views;
    private UserDto author;
    private Topic topic;
    private CommentDto lastComment = null;
    private Integer noOfComments = 0;
    private String status;

}
