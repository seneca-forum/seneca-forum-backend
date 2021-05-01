package com.seneca.senecaforum.client.controller;

import com.seneca.senecaforum.client.exception.ErrorConstants;
import com.seneca.senecaforum.client.exception.NotFoundException;
import com.seneca.senecaforum.domain.Post;
import com.seneca.senecaforum.repository.TopicRepository;
import com.seneca.senecaforum.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class PostController {

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private PostService postService;

    @GetMapping("/posts/{topicId}")
    public ResponseEntity<List<Post>> getAllPostByTopic(@PathVariable int topicId){
        if (topicRepository.findById(topicId).isEmpty()){
            throw new NotFoundException("Topic "+ ErrorConstants.NOT_FOUND);
        }
        return ResponseEntity.ok(postService.getAllPostByTopic(topicId));
    }
}
