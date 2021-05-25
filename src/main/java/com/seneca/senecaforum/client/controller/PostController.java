package com.seneca.senecaforum.client.controller;

import com.seneca.senecaforum.domain.Post;
import com.seneca.senecaforum.domain.Tag;
import com.seneca.senecaforum.domain.Topic;
import com.seneca.senecaforum.domain.User;
import com.seneca.senecaforum.repository.PostRepository;
import com.seneca.senecaforum.repository.TagRepository;
import com.seneca.senecaforum.repository.TopicRepository;
import com.seneca.senecaforum.repository.UserRepository;
import com.seneca.senecaforum.service.PostService;
import com.seneca.senecaforum.service.dto.PostDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/posts")
public class PostController {
    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private PostService postService;


//    @GetMapping
//    public ResponseEntity<Set<Topic>>getAllPostsByPostID(){
//        List<Topic> topics = topicRepository.findAll();
//        Set<Topic>topicSet = new TreeSet<>();
//        for(Topic t:topics){
//            topicSet.add(t);
//        }
//        return ResponseEntity.ok(topicSet);
//    }

    @GetMapping("/{postId}")
    public ResponseEntity<Post>getPostByPostID(@PathVariable("postId")  Integer postId){
        Post post = postRepository.findById(postId).get();
        return ResponseEntity.ok(post);
    }

    @PostMapping("/newpost")
    public ResponseEntity<String>createNewPost(@RequestBody PostDto p){
        User user = userRepository.findByUserId(p.getAuthor().getUserId()).get();

        Topic topic = topicRepository.findById(p.getTopic().getTopicId()).get();
        Post post = new Post().builder()
                .title(p.getTitle())
                .content(p.getContent())
                .createdOn(new Date())
                .author(user)
                .topic(topic)
                .tags(p.getTags())
                .views(0)
                .build();

        Post savedPost = postRepository.save(post);
        // store the tags
        List<Tag>tags = tagRepository.findAll();
        List<String>tagsDb = tags.stream().map(Tag::getTagName).collect(Collectors.toList());
        List<String>tagsUsr = Arrays.stream(savedPost.getTags().toLowerCase().split("#")).collect(Collectors.toList());
        tagsUsr.removeAll(tagsDb);
        tagsUsr.remove(tagsUsr.indexOf(""));
        tagsUsr.forEach(t->{
            Tag newTag = new Tag().builder().tagName(t).build();
            tagRepository.save(newTag);
        });
        return new ResponseEntity("Created new post successfully", HttpStatus.OK);
    }
}
