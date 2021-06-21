package com.seneca.senecaforum.client.controller;

import com.seneca.senecaforum.client.exception.ErrorConstants;
import com.seneca.senecaforum.client.exception.InternalException;
import com.seneca.senecaforum.client.exception.NotFoundException;
import com.seneca.senecaforum.domain.Comment;
import com.seneca.senecaforum.domain.Post;
import com.seneca.senecaforum.domain.Topic;
import com.seneca.senecaforum.domain.UserEntity;
import com.seneca.senecaforum.service.PostService;
import com.seneca.senecaforum.service.TagService;
import com.seneca.senecaforum.service.TopicService;
import com.seneca.senecaforum.service.UserService;
import com.seneca.senecaforum.service.constants.ApplicationConstants;
import com.seneca.senecaforum.service.dto.CommentDto;
import com.seneca.senecaforum.service.dto.PostDetailDto;
import com.seneca.senecaforum.service.dto.PostSearchDto;
import com.seneca.senecaforum.service.dto.PostViewDto;
import com.seneca.senecaforum.service.utils.MapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/posts")
public class PostController {
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
        Optional<Post> postFind = postService.getPostByPostId(postId);
        if (postFind.isPresent()){
            return ResponseEntity.ok(postFind.get());
        }else {
            throw new NotFoundException("Post "+ ErrorConstants.NOT_FOUND);
        }
    }

    @PostMapping()
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<Post>createNewPost(@RequestBody PostDetailDto p) throws URISyntaxException {
        if(p.getTags() == null || p.getTags().isEmpty()){
            p.setTags("");
        }
        UserEntity userEntity = userService.getUserByUsername(p.getAuthor().getUsername());
        Topic topic = topicService.getTopicByTopicId(p.getTopic().getTopicId());
        Post post = postService.createNewPost(p, userEntity, topic);

        // store the tags
        tagService.createTag(p.getTags());
        return ResponseEntity.created(
                new URI(ApplicationConstants.BASE_URL+"posts/"+p.getPostId()))
                .body(post);

    }

    @PostMapping("/{postId}/comments")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<List<Comment>>createNewComment(
            @PathVariable("postId")  Integer postId,
            @RequestBody CommentDto c) throws URISyntaxException {
        Optional<Post> postFind = postService.getPostByPostId(postId);
        if (postFind.isEmpty()){
            throw new NotFoundException("Post "+ ErrorConstants.NOT_FOUND);
        }
        Post post = postFind.get();
        UserEntity userEntity = userService.getUserByUsername(c.getCommenter().getUsername());
        Optional<Post> savePost = postService.createNewComment(post,userEntity,c);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(new URI(ApplicationConstants.BASE_URL+postId+"/comments"));
        return savePost.map(p ->
                ResponseEntity.ok(p.getComments())).orElseGet(() ->
                ResponseEntity.noContent().headers(headers).build());
    }

    @PutMapping()
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<Post>editAPost(@RequestBody PostDetailDto p) throws URISyntaxException {
        // store the tags
        if(p.getTags() == null || p.getTags().isEmpty()){
            throw new InternalException("Cannot create new tags");
        }else {
            Optional<Post> editedPost = postService.editAPost(p);
            tagService.createTag(p.getTags());
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(new URI(ApplicationConstants.BASE_URL+"/news/"+p.getPostId()));
            if (editedPost.isEmpty()){
                return ResponseEntity.noContent().headers(headers).build();
            }else {
                return ResponseEntity.ok(editedPost.get());
            }
        }
    }

    @GetMapping("/hot")
    public ResponseEntity<List<PostViewDto>>getHotPosts(
            @RequestParam(defaultValue = "1") Integer page
    ){
        List<PostViewDto> viewPosts = postService.getHotPosts(page);
        return ResponseEntity.ok(viewPosts);
    }

    @GetMapping()
    public ResponseEntity<List<PostSearchDto>>searchPostsByContent(
            @RequestParam(required = false) String content
    ){
        List<PostSearchDto>posts = postService.searchPostsByContent(content);
        System.out.println(posts);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/all")
    public ResponseEntity<List<PostViewDto>>getAllPosts(){
        List<PostViewDto>posts;
        if(postService.hasPending()){
            posts = postService.getAllPostsOrderByPending();
        }
        else{
            posts = postService.getAllPostsOrderByCreatedOn();
        }
        return ResponseEntity.ok(posts);
    }

    @PutMapping("/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void>updatePostsStatus(
            @RequestParam(required = false) String status
            ,@RequestBody List<Integer>postIds) throws URISyntaxException {
        postService.updatePostsStatus(postIds, status);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(new URI(ApplicationConstants.BASE_URL+"/posts?"+"status="+status));
        return ResponseEntity.noContent().headers(headers).build();
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<List<PostViewDto>> getAllPostsByUserId(
            @PathVariable("userId") String userId
    ) {
        if(!userService.isUserIdValid(userId)){
            throw new NotFoundException("Cannot find any users with userId "+userId);
        }
        List<Post>posts = postService.getAllPostsByUserIdOrderByPending(userId);
        List<PostViewDto>postDtos = MapperUtils.mapperList(posts,PostViewDto.class);
        return ResponseEntity.ok(postDtos);
    }

    @DeleteMapping("/{postId}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<Void> deletePost(
            @PathVariable("postId")  int postId) throws URISyntaxException {
        if(!postService.isPostIdValid(postId)){
            throw new NotFoundException("Cannot find any post with postId "+postId);
        }
        postService.deleteAPost(postId);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(new URI(ApplicationConstants.BASE_URL+"/posts/"+postId));
        return ResponseEntity.noContent().headers(headers).build();
    }

    @GetMapping("/size")
    public ResponseEntity<Integer>getNoOfAllPosts(){
        Integer noOfAllPosts = postService.getNoOfAllPosts();
        return ResponseEntity.ok(noOfAllPosts);
    }
}
