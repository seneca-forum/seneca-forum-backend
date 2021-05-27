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
public class PostDetailDto {
    private Integer postId;
    private String title;
    private Date createdOn;
    private UserDto author;
    private Topic topic;
    private List<CommentDto> comments;
    private String tags;
    private Integer views;
    private String content;

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
        if(!(obj instanceof PostDetailDto)){
            return false;
        }
        PostDetailDto postDetailDto = (PostDetailDto) obj;
        if(this.postId==null&& postDetailDto.getPostId()!=null){
            return false;
        }
        else if(this.postId!=null && !this.postId.equals(postDetailDto.getPostId())){
            return false;
        }
        return true;
    }
}
