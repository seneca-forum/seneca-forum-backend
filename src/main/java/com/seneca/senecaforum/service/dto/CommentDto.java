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

//    @Override
//    public int compareTo(CommentDto o) {
//        return o.createdOn.compareTo(createdOn);
//    }
}
