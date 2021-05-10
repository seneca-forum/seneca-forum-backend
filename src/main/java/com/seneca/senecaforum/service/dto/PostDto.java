package com.seneca.senecaforum.service.dto;

import com.seneca.senecaforum.domain.Topic;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostDto {
    private Integer postId;
    private String title;
    private Date createdOn;
    private UserDto author;
    private Topic topic;
    private String tags;
    private Integer views;

    @Override
    public int hashCode() {
        int prime = 31;
        return prime+ ((postId==null)?0:prime+postId.hashCode());
    }

    @Override
    public boolean equals(Object obj) {
        if(obj==this){
            return true;
        }
        if(!(obj instanceof PostDto)){
            return false;
        }
        PostDto postDto = (PostDto) obj;
        if(this.postId==null&&postDto.getPostId()!=null){
            return false;
        }
        else if(this.postId!=null && !this.postId.equals(postDto.getPostId())){
            return false;
        }
        return true;
    }
}
