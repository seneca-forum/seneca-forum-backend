package com.seneca.senecaforum.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.seneca.senecaforum.domain.Comment;
import com.seneca.senecaforum.domain.Post;
import com.seneca.senecaforum.domain.Topic;
import com.seneca.senecaforum.domain.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;
import java.util.TreeSet;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostDto implements EntityDto{
    private Integer postId;
    private String title;
    private Date createdOn;
    private UserDto author;
    private Topic topic;
    private Set<CommentDto> comments = new TreeSet();
    private String tags;

}
