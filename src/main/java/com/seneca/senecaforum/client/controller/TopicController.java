package com.seneca.senecaforum.client.controller;

import com.seneca.senecaforum.domain.Topic;
import com.seneca.senecaforum.repository.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.http.HttpHeaders;
import java.util.List;

@RestController
@RequestMapping("/api")
public class TopicController {
    @Autowired
    private TopicRepository topicRepository;

    @GetMapping("/topics")
    public ResponseEntity<List<Topic>> getAllTopics() {
        List<Topic> topics = topicRepository.findAll();
        if(topics.size()==0){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(topics);
    }


}
