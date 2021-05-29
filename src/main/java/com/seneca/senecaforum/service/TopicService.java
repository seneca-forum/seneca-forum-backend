package com.seneca.senecaforum.service;

import com.seneca.senecaforum.domain.Post;
import com.seneca.senecaforum.domain.Topic;
import com.seneca.senecaforum.repository.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TopicService {
    @Autowired
    TopicRepository topicRepository;

    public List<Topic>getAllTopics(){
        return topicRepository.findAll();
    }

    public Topic getTopicByTopicId(int topicId )
    {
        return topicRepository.findById(topicId).get();
    }

    public List<Topic>getSortedTopicsByNoofCmtsPosts(){
        return topicRepository.getSortedTopicsByNoOfPostsCmts();
    }
}