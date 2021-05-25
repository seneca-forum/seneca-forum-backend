package com.seneca.senecaforum.client.controller;

import com.seneca.senecaforum.client.exception.BadRequestException;
import com.seneca.senecaforum.client.exception.InternalException;
import com.seneca.senecaforum.domain.*;
import com.seneca.senecaforum.repository.PostRepository;
import com.seneca.senecaforum.repository.TagRepository;
import com.seneca.senecaforum.repository.TopicRepository;
import com.seneca.senecaforum.repository.UserRepository;
import com.seneca.senecaforum.service.PostService;
import com.seneca.senecaforum.service.TagService;
import com.seneca.senecaforum.service.TopicService;
import com.seneca.senecaforum.service.UserService;
import com.seneca.senecaforum.service.dto.CommentDto;
import com.seneca.senecaforum.service.dto.PostDto;
import com.seneca.senecaforum.service.utils.MapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/posts")
@CrossOrigin(origins = "*")
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

    @Autowired
    private TagService tagService;

    @Autowired
    private TopicService topicService;

    @Autowired
    private UserService userService;

    @GetMapping("/{postId}")
    public ResponseEntity<Post>getPostByPostID(@PathVariable("postId")  Integer postId){
        Post post = postService.getPostByPostId(postId);
        return ResponseEntity.ok(post);
    }

    @PostMapping()
    public ResponseEntity<String>createNewPost(@RequestBody PostDto p){
        User user = userService.getUserByUsername(p.getAuthor().getUsername());

        Topic topic = topicService.getTopicByTopicId(p.getTopic().getTopicId());

        Post post = postService.createNewPost(p, user, topic);

        // store the tags
        if(p.getTags()!=null && p.getTags() != ""){
            try{
                tagService.createTag(post);
            }
            catch(Exception ex){
                throw new InternalException("Cannot create new tags");
            }
        }
        return new ResponseEntity("Created new post successfully", HttpStatus.OK);
    }

    @PostMapping("/{postId}/comments")
    public ResponseEntity<List<Comment>>createNewComment(
            @PathVariable("postId")  Integer postId,
            @RequestBody CommentDto c){
        Post post = postService.getPostByPostId(postId);
        User user = userService.getUserByUsername(c.getCommenter().getUsername());
        Post savedPost = null;
        try{
            savedPost = postService.createNewComment(post, user, c);
        }
        catch(Exception ex){
            throw new InternalException("Cannot save a new comment");
        }
        return new ResponseEntity(savedPost.getComments(),HttpStatus.OK);
    }

    @PutMapping()
    public ResponseEntity<Post>editAPost(@RequestBody PostDto p){
        Post savedPost = postService.editAPost(p);
        // store the tags
        if(p.getTags()!=null && p.getTags() != ""){
            try{
                tagService.createTag(savedPost);
            }
            catch (Exception ex){
                throw new InternalException("Cannot create new tags");
            }
        }
        return new ResponseEntity(savedPost, HttpStatus.OK);
    }
}
