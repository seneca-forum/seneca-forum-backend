package com.seneca.senecaforum.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CommentDto {
    private Integer commentId;

    private Date createdOn;

    private UserDto commenter;

    private String content;

    private Boolean enabled;

}
