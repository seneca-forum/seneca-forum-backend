package com.seneca.senecaforum.service;

import com.seneca.senecaforum.domain.Topic;
import com.seneca.senecaforum.repository.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TopicService {
    @Autowired
    TopicRepository topicRepository;

    public Topic getTopicByTopicId(int topicId ){
        return topicRepository.findById(topicId).get();
    }
}