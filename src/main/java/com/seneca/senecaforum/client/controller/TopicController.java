package com.seneca.senecaforum.client.controller;

import com.seneca.senecaforum.client.exception.ErrorConstants;
import com.seneca.senecaforum.client.exception.NotFoundException;
import com.seneca.senecaforum.domain.Topic;
import com.seneca.senecaforum.repository.PostRepository;
import com.seneca.senecaforum.repository.TopicRepository;
import com.seneca.senecaforum.repository.UserRepository;
import com.seneca.senecaforum.service.PostService;
import com.seneca.senecaforum.service.dto.PostViewDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.*;

//class PostComparatorByComment implements Comparator<PostDto> {
//    @Override
//    public int compare(PostDto p1, PostDto p2) {
//        if(p1.getComments().size()>0&&p2.getComments().size()>0){
//            // Comment already sorts them in desc order
//            Collections.sort(p1.getComments(),(c1,c2)->c2.getCreatedOn().compareTo(c1.getCreatedOn()));
//            Collections.sort(p2.getComments(),(c1,c2)->c2.getCreatedOn().compareTo(c1.getCreatedOn()));
//            CommentDto myRecentComment= p1.getComments().get(0);
//            CommentDto otherRecentComment= p2.getComments().get(0);
//            return otherRecentComment.getCreatedOn().compareTo(myRecentComment.getCreatedOn());
//        }
//        else if(p1.getComments().size()>0&&p2.getComments().size()==0){
//            return -1;
//        }
//        else if(p1.getComments().size()==0&&p2.getComments().size()>0){
//            return 1;
//        }
//        else{
//            return p2.getPostId().compareTo(p1.getPostId());
//        }
//
//    }
//}


@RestController
@RequestMapping("/api/topics")
public class TopicController {
    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostService postService;


    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public ResponseEntity<Set<Topic>> getAllTopics() {
        List<Topic> topics = topicRepository.findAll();
        Set<Topic> topicSet = new HashSet<>();
        for (Topic t : topics) {
            topicSet.add(t);
        }
        return ResponseEntity.ok(topicSet);
    }

    @GetMapping("/{topicId}/posts/size")
    public ResponseEntity<Integer>getPostSizeFromTopicID(@PathVariable Integer topicId){
        int postSize = postRepository.getPostSizeByTopicId(topicId);
        return ResponseEntity.ok(postSize);
    }

    @GetMapping("/{topicId}/posts")//default:comment-desc
    public ResponseEntity<List<PostViewDto>> getAllPostByTopic(
            @PathVariable Integer topicId,
            @RequestParam(required = false) String order,
            @RequestParam(required = false) String s,
            @RequestParam(required = false) String e,
            @RequestParam(defaultValue = "1") Integer p,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String tags
    ) throws ParseException {
        Optional<Topic> topic = topicRepository.findById(topicId);
            if (topic.isPresent()) {
                return ResponseEntity.ok(
                        postService.getAllPostByTopic(topic.get(),order,s,e,p,sortBy,tags));
            } else {
                throw new NotFoundException("Topic "+ ErrorConstants.NOT_FOUND);
            }
    }

}
