package com.seneca.senecaforum.service;


import com.seneca.senecaforum.domain.Topic;
import com.seneca.senecaforum.repository.PostRepository;
import com.seneca.senecaforum.repository.TopicRepository;
import com.seneca.senecaforum.service.dto.TopicDto;
import com.seneca.senecaforum.service.utils.MapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TopicService {
    @Autowired
    TopicRepository topicRepository;

    @Autowired
    PostRepository postRepository;

    public List<Topic>getAllTopics(){
        return topicRepository.findAll();
    }

    public Topic getTopicByTopicId(String topicId )
    {
        return topicRepository.findById(topicId).get();
    }

    public TopicDto getTopicViewByTopicId(String topicId){
        Optional<Topic>topic = topicRepository.findById(topicId);
        if(topic.isEmpty()){
            return null;
        }
        TopicDto topicDto = MapperUtils.mapperObject(topic.get(),TopicDto.class);
        topicDto.setViews((postRepository.getNoOfViewsByTopicId(topicId)==null)?0: postRepository.getNoOfViewsByTopicId(topicId));
        topicDto.setNoOfPosts(postRepository.getNoOfPostsByTopicId(topicId));
        return topicDto;
    }
}