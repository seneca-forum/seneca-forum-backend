package com.seneca.senecaforum.service;


import com.seneca.senecaforum.domain.Topic;
import com.seneca.senecaforum.repository.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TopicService {
    @Autowired
    TopicRepository topicRepository;

    public List<Topic>getAllTopics(){
        return topicRepository.findAll();
    }

    public Topic getTopicByTopicId(String topicId )
    {
        return topicRepository.findById(topicId).get();
    }

    public List<Topic>getSortedTopicsByNoofCmtsPosts(){
        return topicRepository.getSortedTopicsByNoOfPostsCmts();
    }
}