package com.seneca.senecaforum.client.controller;

import com.seneca.senecaforum.client.exception.ErrorConstants;
import com.seneca.senecaforum.client.exception.NotFoundException;
import com.seneca.senecaforum.domain.Topic;
import com.seneca.senecaforum.repository.PostRepository;
import com.seneca.senecaforum.repository.TopicRepository;
import com.seneca.senecaforum.repository.UserRepository;
import com.seneca.senecaforum.service.PostService;
import com.seneca.senecaforum.service.dto.CommentDto;
import com.seneca.senecaforum.service.dto.PostDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.*;

class CommentComparator implements Comparator<CommentDto>{
    @Override
    public int compare(CommentDto o1, CommentDto o2) {
        return o2.getCreatedOn().compareTo(o1.getCreatedOn());
    }
}

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
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

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

    //    @GetMapping("/{topicId}/posts/size")
//    public ResponseEntity<Integer>getNumberofPostsFromTopicID(@PathVariable Integer topicId){
//        List<Post>posts = postRepository.findAllByTopicId(topicId);
//        Integer postSize = posts.size();
//        return ResponseEntity.ok(postSize);
//    }
    @GetMapping("/{topicId}/posts")//default:comment-desc
    public ResponseEntity<List<PostDto>> getAllPostByTopic(
            @PathVariable Integer topicId,
            @RequestParam(required = false) String order,
            @RequestParam(required = false) String start,
            @RequestParam(required = false) String end,
            @RequestParam(defaultValue = "1") Integer p,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String name
    ) throws ParseException {
        Optional<Topic> topic = topicRepository.findById(topicId);
            if (topic.isPresent()) {
                return ResponseEntity.ok(
                        postService.getAllPostByTopic(topic.get(),order,start,end,p,sortBy,name));
            } else {
                throw new NotFoundException("Topic "+ ErrorConstants.NOT_FOUND);
            }
    }



//    @GetMapping("/{topicId}/posts")
//    public ResponseEntity<Set<PostDto>>getAllPostsByTopicID(
//            @PathVariable Integer topicId,
//            @RequestParam Integer p,
//            @RequestParam(value="tags",required = false) String tag,
//            @RequestParam(value="start",required = false) String start,
//            @RequestParam(value="end",required = false) String end){
//        Date date = java.sql.Date.valueOf(start);
//        java.sql.Date endDate = java.sql.Date.valueOf(end);
//        List<Post>posts = new ArrayList<>();
//        if(tag!=null&&start!=null&&end!=null){
//            posts = postRepository.filterPosts(topicId,tag,startDate,endDate);
//        }
//        else{
//            posts = postRepository.findAllByTopicId(topicId);
//        }
//
//
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
//        Integer startPage = (pageIndex-1)*pageSize;
//        List<PostDto>postList = new ArrayList<>(postDtoSet);
//        PostDto startPost = postList.get(startPage);
//
//        Integer endPage = (startPage+pageSize)>=postDtoSet.size()?(startPage+postDtoSet.size()-startPage-1):(startPage+pageSize);
//        PostDto endPost = postList.get(endPage);
//
//        Set<PostDto> postSubset = new TreeSet<PostDto>(new PostComparator<PostDto>() {
//            @Override
//            public int compare(PostDto o1, PostDto o2) {
//                // Define comparing logic here
//                return o1.getPostId().compareTo(o2.getPostId());
//            }
//        });
//        postSubset = postDtoSet.subSet(startPost,endPost);
//        // add the last item to return in case it is in the last page
//        if((startPage+pageSize)>=postDtoSet.size()){
//            Set<PostDto>subSetWithLast = new TreeSet<>();
//            subSetWithLast.addAll(postSubset);
//            subSetWithLast.add(postDtoSet.last());
//            return ResponseEntity.ok(subSetWithLast);
//        }
//
//        return ResponseEntity.ok(postSubset);
//    }
//
//    @GetMapping("/{topicId}/posts")
//    public ResponseEntity<List<PostDto>>getAllPostsByTopicID(
//            @PathVariable Integer topicId,
//            @RequestParam Integer pageIndex,
//            @RequestParam Integer pageSize,
//            @RequestParam(value="tag",required = false) String tag,
//            @RequestParam(value="start",required = false) String start,
//            @RequestParam(value="end",required = false) String end,
//            @RequestParam(value="sorted",required=false)String sorted,
//            @RequestParam(value="orderby",required=false)String orderby){
//        List<Post>posts = new ArrayList<>();
//        if(tag!=null&&start!=null&&end!=null){
//            java.sql.Date startDate = java.sql.Date.valueOf(start);
//            java.sql.Date endDate = java.sql.Date.valueOf(end);
//            posts = postRepository.filterPosts(topicId,tag,startDate,endDate);
//        }
//        else{
//            posts = postRepository.findAllByTopicId(topicId);
//        }
//        List<PostDto>postsDto = new ArrayList<>();
//        for(Post p:posts){
//            // convert author
//            User author = p.getAuthor();
//            UserDto dtoAuthor = (UserDto) MapperUtils.convertToDto(author,new UserDto());
//
//            // convert comment
//            List<CommentDto> commentsDto = new ArrayList<>();
//            for(Comment c:p.getComments()){
//                EntityDto dtoCmt = MapperUtils.convertToDto(c,new CommentDto());
//                commentsDto.add((CommentDto)dtoCmt);
//            }
//            Collections.sort(commentsDto,(p1,p2)->p2.getCreatedOn().compareTo(p1.getCreatedOn()));
//            // convert post
//            PostDto convertedPostDto = new PostDto().builder()
//                    .postId(p.getPostId())
//                    .title(p.getTitle())
//                    .createdOn(p.getCreatedOn())
//                    .author(dtoAuthor)
//                    .topic(p.getTopic())
//                    .comments(commentsDto)
//                    .tags(p.getTags())
//                    .views(p.getViews())
//                    .build();
//
//            postsDto.add(convertedPostDto);
//        }
//        if(sorted=="posts"&&orderby=="latest"){
//            Collections.sort(postsDto,(p1,p2)->p2.getPostId().compareTo(p1.getPostId()));
//        }
//        else if(sorted=="posts"&&orderby=="oldest"){
//            Collections.sort(postsDto,(p1,p2)->p1.getPostId().compareTo(p2.getPostId()));
//        }
//        else if(sorted=="comments"&&orderby=="oldest"){
//            Collections.sort(postsDto,new PostComparatorByComment());
//            Collections.reverse(postsDto);
//        }
//        Collections.sort(postsDto,new PostComparatorByComment());
////        Collections.reverse(postsDto);
//        //Collections.reverse(postsDto);
//        return ResponseEntity.ok(postsDto);
//
}
