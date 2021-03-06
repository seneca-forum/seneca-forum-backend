package com.seneca.senecaforum.service;

import com.seneca.senecaforum.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;


    public int getNoOfCommentsByTopicId(String topicId){
        return commentRepository.getNoOfCommentsByTopicId(topicId);
    }
}
