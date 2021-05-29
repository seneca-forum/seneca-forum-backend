package com.seneca.senecaforum.client.controller;

import com.seneca.senecaforum.client.exception.InternalException;
import com.seneca.senecaforum.domain.*;
import com.seneca.senecaforum.service.*;
import com.seneca.senecaforum.service.dto.CommentDto;
import com.seneca.senecaforum.service.dto.PostDetailDto;
import com.seneca.senecaforum.service.dto.PostSearchDto;
import com.seneca.senecaforum.service.dto.PostViewDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/posts")
@CrossOrigin(origins = "*")
public class PostController {
    @Autowired
    private PostService postService;

    @Autowired
    private TagService tagService;

    @Autowired
    private TopicService topicService;

    @Autowired
    private UserService userService;

    @Autowired
    private CommentService commentService;

    @GetMapping("/{postId}")
    public ResponseEntity<Post>getPostByPostID(@PathVariable("postId")  Integer postId){
        Post post = postService.getPostByPostId(postId);
        return ResponseEntity.ok(post);
    }

    @PostMapping()
    public ResponseEntity<Post>createNewPost(@RequestBody PostDetailDto p){
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
        return new ResponseEntity(post, HttpStatus.OK);
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
    public ResponseEntity<Post>editAPost(@RequestBody PostDetailDto p){
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

    @GetMapping("/hot")
    public ResponseEntity<List<PostViewDto>>getHotPosts(){
        List<PostViewDto> viewPosts = postService.getHotPosts();
        viewPosts.forEach(p->p.setNoOfComments(postService.getNoOfCommentsByPostId(p.getPostId())));
        return ResponseEntity.ok(viewPosts);
    }

    @GetMapping()
    public ResponseEntity<List<PostSearchDto>>searchPostsByContent(@RequestParam(required = true) String content){
        List<PostSearchDto>posts = postService.searchPostsByContent(content);
        System.out.println(posts);
        return ResponseEntity.ok(posts);
    }

}
