package com.seneca.senecaforum.service;

import com.seneca.senecaforum.client.exception.BadRequestException;
import com.seneca.senecaforum.domain.Comment;
import com.seneca.senecaforum.domain.Post;
import com.seneca.senecaforum.domain.Topic;
import com.seneca.senecaforum.domain.User;
import com.seneca.senecaforum.repository.PostRepository;
import com.seneca.senecaforum.service.dto.CommentDto;
import com.seneca.senecaforum.service.dto.PostDetailDto;
import com.seneca.senecaforum.service.dto.PostSearchDto;
import com.seneca.senecaforum.service.dto.PostViewDto;
import com.seneca.senecaforum.service.utils.ApplicationUtils;
import com.seneca.senecaforum.service.utils.ContentConverterUtils;
import com.seneca.senecaforum.service.utils.MapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PostService {
    @Autowired
    private PostRepository postRepository;


    public List<PostViewDto> getAllPostByTopic(
            Topic topic,String methodOrder,String start,String end,int page,String sortBy,String tags
    ) throws ParseException {
        List<Post> posts = null;
        Date startDate = null;
        Date endDate = null;
        if (Objects.nonNull(start) && Objects.nonNull(end)) {
            startDate = ApplicationUtils.convertToDate(start);
            endDate = ApplicationUtils.convertToDate(end);
        }
        boolean checkOrder = Objects.isNull(methodOrder);
        if (Objects.nonNull(sortBy) && sortBy.equals("posts")) {
            if (checkOrder || methodOrder.equals("desc"))
                posts = postRepository.findPostsByTopicBasedOnPost(
                        topic, tags, startDate, endDate,
                        PageRequest.of(page - 1, 10, Sort.by(Sort.Direction.DESC, "createdOn")));
            else
                posts = postRepository.findPostsByTopicBasedOnPost(
                        topic, tags, startDate, endDate,
                        PageRequest.of(page - 1, 10, Sort.by(Sort.Direction.ASC, "createdOn")));
        } else if (Objects.isNull(sortBy) || sortBy.equals("comments")) {
            posts = postRepository.findPostsByTopicBasedOnComment(
                    topic, methodOrder, tags, PageRequest.of(page - 1, 10));
        }
        if (posts.size() == 0) {
            return null;
        }
        List<PostViewDto> postPage = MapperUtils.mapperList(posts, PostViewDto.class);
        for (int i = 0; i < posts.size(); ++i) {
            int noOfComments = posts.get(i).getComments().size();
            if (noOfComments == 0){
                postPage.get(i).setNoOfComments(noOfComments);
                continue;
            }
            if (Objects.isNull(methodOrder) || methodOrder.equals("desc")) {
                postPage.get(i).setLastComment(
                        MapperUtils.mapperObject(posts.get(i).getComments().get(noOfComments - 1), CommentDto.class));
            } else {
                postPage.get(i).setLastComment(
                        MapperUtils.mapperObject(posts.get(i).getComments().get(0), CommentDto.class));
            }
            postPage.get(i).setNoOfComments(noOfComments);
        }
        return postPage;

    }

    public Post getPostByPostId(Integer postId){
        Post post = null;
        try {
            post = postRepository.findById(postId).get();
        } catch (Exception ex) {
            throw new BadRequestException("Found nothing with postId " + postId);
        }
        Collections.sort(post.getComments(), (c1, c2) -> c2.getCreatedOn().compareTo(c1.getCreatedOn()));
        return post;
    }

    public Post createNewPost (PostDetailDto p, User user, Topic topic){
        // trick to avoid storing null in database, for query purposes
        if(p.getTags()==null){
            p.setTags("");
        }
        Post post = Post.builder()
                .title(p.getTitle())
                .content(p.getContent())
                .createdOn(new Date())
                .author(user)
                .topic(topic)
                .tags(p.getTags())
                .views(0)
                .build();

        return postRepository.save(post);
    }

    public Post createNewComment (Post post, User user, CommentDto c){
        Comment newComment = Comment.builder()
                .commenter(user)
                .content(c.getContent())
                .createdOn(new Date())
                .enabled(c.getEnabled())
                .build();
        post.addComment(newComment);
        Post savedPost = postRepository.save(post);
        Collections.sort(savedPost.getComments(), (c1, c2) -> c2.getCreatedOn().compareTo(c1.getCreatedOn()));
        return post;
    }

    public Post editAPost (PostDetailDto p){
        Post savedPost = postRepository.findById(p.getPostId()).get();
        savedPost.setContent(p.getContent());
        savedPost.setTags(p.getTags());
        savedPost.setTitle(p.getTitle());
        savedPost.setTopic(p.getTopic());
        return postRepository.save(savedPost);
    }

    public List<PostViewDto>getHotPosts(){
        List<PostViewDto>viewPosts = null;
        List<Post>posts = postRepository.getHotPosts(PageRequest.of(0,10));
        viewPosts = MapperUtils.mapperList(posts,PostViewDto.class);
        return viewPosts;
    }

    public int getNoOfCommentsByPostId(int postId){
        Optional<Post>post = postRepository.findById(postId);
        if(post.isEmpty()){
            return 0;
        }
        return post.get().getComments().size();
    }

    public int getNoOfPostsByTopicId(String topicId){
        return postRepository.getNoOfPostsByTopicId(topicId);
    }

    public List<PostSearchDto> searchPostsByContent(String kw){
        String keyword = kw.toLowerCase(Locale.ROOT);
        List<Post>foundPosts = new ArrayList<>();
        List<Post>posts = postRepository.findPostsContainKeywords(keyword);
        posts.forEach(p->{
            String text = p.getContent().toLowerCase(Locale.ROOT);
            String result = ContentConverterUtils.extractContentWithKeywords(text,keyword);
            if(result!=null){
                p.setContent(result);
                foundPosts.add(p);
            }
        });
        List<PostSearchDto> searchDtos = MapperUtils.mapperList(foundPosts,PostSearchDto.class);
        searchDtos.forEach(p->{
            if(keyword.split(" ").length==1){
                List<Integer>idxs = ContentConverterUtils.getIdxsShortKeywords(p.getContent(),keyword);
                p.setIdxKeywords(idxs);
            }
            else{
                List<Integer>idxs = ContentConverterUtils.getIdxsLongKeywords(p.getContent(),keyword);
                p.setIdxKeywords(idxs);
            }
        });
        return searchDtos;
    }

    public List<Post>getAllPostsByUserId(String userId){
        return postRepository.getAllPostsByUserId(userId);
    }

    public boolean isPostIdValid(int postId){
        Optional<Post>post = postRepository.findById(postId);
        if(post.isPresent()){
            return true;
        }
        return false;
    }

    public void deleteAPost(int postId){
        Post post = postRepository.findById(postId).get();
        postRepository.delete(post);
    }

    public List<Post> getAllPostsOrderByPending(){
        return postRepository.getAllPostsOrderByPending();
    }

    public List<Post> getAllPostsOrderByCreatedOn(){
        List<Post>posts = postRepository.findAll()
                .stream()
                .sorted((p1,p2)->p2.getCreatedOn().compareTo(p1.getCreatedOn()))
                .collect(Collectors.toList());
        return posts;
    }

    public void updatePostsStatus(List<Integer> postIds, String status) {
        postIds.forEach(id->{
            Optional<Post> p = postRepository.findById(id);
            if(p.isPresent()){
                p.get().setStatus(status);
            }
            postRepository.save(p.get());
        });
    }

    public boolean hasPending() {
        List<Post>posts = postRepository.findAll()
                .stream()
                .filter(post -> post.getStatus().equals("pending"))
                .collect(Collectors.toList());
        return posts.size()>0;
    }
}
