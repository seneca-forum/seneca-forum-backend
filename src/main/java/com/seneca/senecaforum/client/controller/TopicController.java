package com.seneca.senecaforum.client.controller;

import com.seneca.senecaforum.client.exception.BadRequestException;
import com.seneca.senecaforum.client.exception.ErrorConstants;
import com.seneca.senecaforum.client.exception.NotFoundException;
import com.seneca.senecaforum.domain.Topic;
import com.seneca.senecaforum.service.CommentService;
import com.seneca.senecaforum.service.PostService;
import com.seneca.senecaforum.service.TopicService;
import com.seneca.senecaforum.service.dto.PostViewDto;
import com.seneca.senecaforum.service.dto.TopicDto;
import com.seneca.senecaforum.service.dto.TopicStatsDto;
import com.seneca.senecaforum.service.utils.MapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
    public ResponseEntity<List<Topic>> getAllTopics() {
        return ResponseEntity.ok(topicService.getAllTopics());
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
            List<PostViewDto> a =  postService.getAllPostByTopic(topic,order,s,e,p,sortBy,tags);
            return ResponseEntity.ok(
                    postService.getAllPostByTopic(topic,order,s,e,p,sortBy,tags));
        }
        catch(Exception ex){
            throw new NotFoundException("Topic "+ ErrorConstants.NOT_FOUND);
        }
    }

    @GetMapping("/{topicId}/stats")
    public ResponseEntity<TopicDto> getTopicStatsByTopicId(@PathVariable String topicId){
        TopicDto topicDto = topicService.getTopicViewByTopicId(topicId);
        if(topicDto==null){
            throw new BadRequestException("Cannot find any topics with this topicId "+topicId);
        }
        return ResponseEntity.ok(topicDto);
    }
}
