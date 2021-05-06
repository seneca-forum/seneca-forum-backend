package com.seneca.senecaforum.client.controller;

import com.seneca.senecaforum.client.exception.BadRequestException;
import com.seneca.senecaforum.client.exception.NotFoundException;
import com.seneca.senecaforum.domain.Comment;
import com.seneca.senecaforum.domain.Post;
import com.seneca.senecaforum.domain.Topic;
import com.seneca.senecaforum.domain.User;
import com.seneca.senecaforum.repository.PostRepository;
import com.seneca.senecaforum.repository.TopicRepository;
import com.seneca.senecaforum.repository.UserRepository;
import com.seneca.senecaforum.service.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/topics")
public class TopicController {
    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public ResponseEntity<Set<Topic>>getAllTopics(){
        List<Topic>topics = topicRepository.findAll();
        Set<Topic>topicSet = new HashSet<>();
        for(Topic t: topics){
            topicSet.add(t);
        }
        return ResponseEntity.ok(topicSet);
    }

//    @GetMapping("/{topicId}/posts/size")
//    public ResponseEntity<Integer>getNumberofPostsFromTopicID(@PathVariable Integer topicId){
//        List<Post>posts = postRepository.findAllByTopicId(topicId);
//        Integer postSize = posts.size();
//        return ResponseEntity.ok(postSize);
//    }
//
//    @GetMapping("/{topicId}/posts")
//    public ResponseEntity<Set<PostDto>>getAllPostsByTopicID(@PathVariable Integer topicId,
//                                                            @RequestParam Integer pageIndex,
//                                                            @RequestParam Integer pageSize){
//        List<Post>posts = postRepository.findAllByTopicId(topicId);
//        if((pageIndex-1)*pageSize>posts.size()){
//            throw new BadRequestException("page "+pageIndex+ " is out of range. Total number of posts are just "+posts.size());
//        }
//        TreeSet<PostDto>postDtoSet =new TreeSet<>();
//
//        for(Post p:posts){
//            // convert author
//            User author = p.getAuthor();
//            UserDto dtoAuthor = (UserDto) MapperUtils.convertToDto(author,new UserDto());
//
//            // convert comment
//            TreeSet<CommentDto> commentSet = new TreeSet<>();
//            Set<Comment> comments = p.getComments();
//            for(Comment c:comments){
//                EntityDto dtoCmt = MapperUtils.convertToDto(c,new CommentDto());
//                commentSet.add((CommentDto)dtoCmt);
//            }
//
//            // convert post
//            PostDto convertedPostDto = new PostDto().builder()
//                    .postId(p.getPostId())
//                    .title(p.getTitle())
//                    .createdOn(p.getCreatedOn())
//                    .author(dtoAuthor)
//                    .topic(p.getTopic())
//                    .comments(commentSet)
//                    .tags(p.getTags())
//                    .views(p.getViews())
//                    .build();
//
//            postDtoSet.add(convertedPostDto);
//        }
//
//        List<Integer>ids = postDtoSet.stream().map(PostDto::getPostId).collect(Collectors.toList());
//        System.out.println(ids);
//        Integer startPage = (pageIndex-1)*pageSize;
//        List<PostDto>postList = new ArrayList<>(postDtoSet);
//        PostDto startPost = postList.get(startPage);
//
//        Integer endPage = (startPage+pageSize)>=postDtoSet.size()?(startPage+postDtoSet.size()-startPage-1):(startPage+pageSize);
//        PostDto endPost = postList.get(endPage);
//
//        Set<PostDto>postSubset = postDtoSet.subSet(startPost,endPost);
//        // add the last item to return in case it is in the last page
//        if((startPage+pageSize)>=postDtoSet.size()){
//            Set<PostDto>subSetWithLast = new TreeSet<>();
//            subSetWithLast.addAll(postSubset);
//            subSetWithLast.add(postDtoSet.last());
//            return ResponseEntity.ok(subSetWithLast);
//        }
//        return ResponseEntity.ok(postSubset);
//    }
}
