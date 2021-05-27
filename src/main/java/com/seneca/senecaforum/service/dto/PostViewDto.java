package com.seneca.senecaforum.service.dto;

import com.seneca.senecaforum.domain.Topic;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostViewDto {

    private Integer postId;
    private String title;
    private Date createdOn;
    private UserDto author;
    private Topic topic;
    private CommentDto lastComment;
    private String tags;
    private Integer views;
    private Integer noOfComments;
    private String content;
}
