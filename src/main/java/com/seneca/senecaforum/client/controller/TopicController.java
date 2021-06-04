package com.seneca.senecaforum.client.controller;

import com.seneca.senecaforum.client.exception.ErrorConstants;
import com.seneca.senecaforum.client.exception.NotFoundException;
import com.seneca.senecaforum.domain.Topic;
import com.seneca.senecaforum.service.CommentService;
import com.seneca.senecaforum.service.PostService;
import com.seneca.senecaforum.service.TopicService;
import com.seneca.senecaforum.service.dto.PostViewDto;
import com.seneca.senecaforum.service.dto.TopicStatsDto;
import com.seneca.senecaforum.service.utils.MapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
@RestController
@RequestMapping("/api/topics")
public class TopicController {
    @Autowired
    private PostService postService;

    @Autowired
    private TopicService topicService;

    @Autowired
    private CommentService commentService;


    @GetMapping
    public ResponseEntity<Set<Topic>> getAllTopics() {
        List<Topic> topics = topicService.getAllTopics();
        Set<Topic> topicSet = new HashSet<>();
        for (Topic t : topics) {
            topicSet.add(t);
        }
        return ResponseEntity.ok(topicSet);
    }

    @GetMapping("/{topicId}/posts/size")
    public ResponseEntity<Integer>getPostSizeFromTopicID(@PathVariable String topicId){
        int postSize = postService.getNoOfPostsByTopicId(topicId);
        return ResponseEntity.ok(postSize);
    }

    @GetMapping("/{topicId}/posts")//default:comment-desc
    public ResponseEntity<List<PostViewDto>> getAllPostByTopic(
            @PathVariable String topicId,
            @RequestParam(required = false) String order,
            @RequestParam(required = false) String s,
            @RequestParam(required = false) String e,
            @RequestParam(defaultValue = "1") Integer p,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String tags
    ) {
        try{
            Topic topic = topicService.getTopicByTopicId(topicId);
            return ResponseEntity.ok(
                    postService.getAllPostByTopic(topic,order,s,e,p,sortBy,tags));
        }
        catch(Exception ex){
            throw new NotFoundException("Topic "+ ErrorConstants.NOT_FOUND);
        }
    }

    @GetMapping("/stats")
    public List<TopicStatsDto>getTopicStatistics(){
        List<Topic>topics = topicService.getSortedTopicsByNoofCmtsPosts();
        List<TopicStatsDto> topicStatDtos = MapperUtils.mapperList(topics, TopicStatsDto.class);
        topicStatDtos.forEach(t->{
            t.setNoOfComments(commentService.getNoOfCommentsByTopicId(t.getTopicId()));
            t.setNoOfPosts(postService.getNoOfPostsByTopicId(t.getTopicId()));
        });
        return topicStatDtos;
    }
}
