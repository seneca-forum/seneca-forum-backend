package com.seneca.senecaforum.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.seneca.senecaforum.domain.Post;
import com.seneca.senecaforum.domain.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CommentDto implements EntityDto, Comparable<CommentDto>{
    private Integer commentId;

    private Date createdOn;

    private UserDto commenter;

    @Override
    public int compareTo(CommentDto o) {
        return o.createdOn.compareTo(createdOn);
    }
}
