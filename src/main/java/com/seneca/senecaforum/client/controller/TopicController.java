package com.seneca.senecaforum.client.controller;

import com.seneca.senecaforum.domain.Comment;
import com.seneca.senecaforum.domain.Post;
import com.seneca.senecaforum.domain.Topic;
import com.seneca.senecaforum.domain.User;
import com.seneca.senecaforum.repository.PostRepository;
import com.seneca.senecaforum.repository.TopicRepository;
import com.seneca.senecaforum.repository.UserRepository;
import com.seneca.senecaforum.service.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@RestController
@RequestMapping("/api/topics")
public class TopicController {
    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public ResponseEntity<List<Topic>>getAllPostsByPostID(){
        List<Topic>topics = topicRepository.findAll();
        return ResponseEntity.ok(topics);
    }

    @GetMapping("/{topicId}/posts")
    public ResponseEntity<List<PostDto>>getAllPostsByTopicID(@PathVariable Integer topicId){
        List<Post>posts = postRepository.findAllByTopicId(topicId);
        List<PostDto>postDto = new ArrayList<>();

        for(Post p:posts){
            // convert author
            User author = p.getAuthor();
            EntityDto dtoAuthor = MapperUtils.convertToDto(author,new UserDto());

            // convert comment
            Set<CommentDto> commentSet = new TreeSet<>();
            Set<Comment> comments = p.getComments();
            for(Comment c:comments){
                EntityDto dtoCmt = MapperUtils.convertToDto(c,new CommentDto());
                commentSet.add((CommentDto)dtoCmt);
            }

            // convert post
            EntityDto dtoPost = MapperUtils.convertToDto(
                    p,
                    new PostDto(
                            p.getPostId(),
                            p.getTitle(),
                            p.getCreatedOn(),
                            (UserDto) dtoAuthor,
                            p.getTopic(),
                            commentSet,
                            p.getTags()
                    )
            );
            postDto.add((PostDto)dtoPost);
        }
        return ResponseEntity.ok(postDto);
    }

}
