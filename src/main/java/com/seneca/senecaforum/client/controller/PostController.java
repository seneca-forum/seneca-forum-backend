package com.seneca.senecaforum.client.controller;

import com.seneca.senecaforum.domain.Topic;
import com.seneca.senecaforum.repository.PostRepository;
import com.seneca.senecaforum.repository.TopicRepository;
import com.seneca.senecaforum.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@RestController
@RequestMapping("/api/posts")
public class PostController {
    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostService postService;


    @GetMapping
    public ResponseEntity<Set<Topic>>getAllPostsByPostID(){
        List<Topic> topics = topicRepository.findAll();
        Set<Topic>topicSet = new TreeSet<>();
        for(Topic t:topics){
            topicSet.add(t);
        }
        return ResponseEntity.ok(topicSet);
    }
}
