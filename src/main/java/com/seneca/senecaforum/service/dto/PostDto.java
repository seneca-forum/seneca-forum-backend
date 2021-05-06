package com.seneca.senecaforum.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.seneca.senecaforum.domain.Comment;
import com.seneca.senecaforum.domain.Post;
import com.seneca.senecaforum.domain.Topic;
import com.seneca.senecaforum.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostDto implements EntityDto,Comparable<PostDto>{
    private Integer postId;
    private String title;
    private Date createdOn;
    private UserDto author;
    private Topic topic;
    private TreeSet<CommentDto> comments = new TreeSet();
    private String tags;
    private Integer views;

    @Override
    public int compareTo(PostDto o) {
        if(this.comments.size()>0&&o.comments.size()>0){
            // Comment already sorts them in desc order
            CommentDto myRecentComment= this.comments.first();
            CommentDto otherRecentComment= o.comments.first();
            return otherRecentComment.getCreatedOn().compareTo(myRecentComment.getCreatedOn());
        }
        else if(this.comments.size()>0&&o.comments.size()==0){
            return -1;
        }
        else if(this.comments.size()==0&&o.comments.size()>0){
            return 1;
        }
        else{
            return o.postId.compareTo(this.postId);
        }
    }

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
