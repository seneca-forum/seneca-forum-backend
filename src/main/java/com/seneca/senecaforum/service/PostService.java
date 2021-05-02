package com.seneca.senecaforum.service;

import com.seneca.senecaforum.repository.PostRepository;
import com.seneca.senecaforum.repository.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private TopicRepository topicRepository;

//    public List<Post> getAllPostByTopic(@Param("topicId") int topicId){
//        Pageable pagination = PageRequest.of(0,10, Sort.by("noOfReplies").descending());
//        List<Post> posts = postRepository.findAllByTopicId(1,pagination);
//        return postRepository.findAllByTopicId(1,pagination);
//    }
}
