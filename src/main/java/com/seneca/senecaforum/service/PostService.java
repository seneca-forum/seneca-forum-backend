package com.seneca.senecaforum.service;

import com.seneca.senecaforum.domain.Comment;
import com.seneca.senecaforum.domain.Post;
import com.seneca.senecaforum.domain.Topic;
import com.seneca.senecaforum.domain.UserEntity;
import com.seneca.senecaforum.repository.PostRepository;
import com.seneca.senecaforum.service.dto.*;
import com.seneca.senecaforum.service.utils.ContentConverterUtils;
import com.seneca.senecaforum.service.utils.MapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.*;

@Service
public class PostService {
    @Autowired
    private PostRepository postRepository;


    public PostsForumResult getAllPostByTopic(
            String topicId,
            String methodOrder,
            String start,
            String end,
            int page,
            String sortBy,
            String tags
    ) throws ParseException {
        List<Post>posts = new ArrayList<>();
        int noOfPosts = 0;
        // without filtering
        if (methodOrder == null && start == null && end == null && sortBy == null && tags == null) {
            posts = this.postRepository.findPostsByTopicIdSortedByLatestComment(topicId, PageRequest.of(page-1,10));
            noOfPosts = postRepository.getNoOfPostsByTopicId(topicId);
        }
        // with filtering
        else{
            posts = this.postRepository.filterPostsBasedOnKeywords(topicId,tags,start, end, sortBy, methodOrder, PageRequest.of(page-1,10));
            noOfPosts = posts.size();
        }

        List<PostViewDto>dtos = MapperUtils.mapperList(posts, PostViewDto.class);
        for (int i = 0; i < dtos.size(); i++){
            if(posts.get(i).getComments().size()>0){
                CommentDto commentDto = MapperUtils.mapperObject(posts.get(i).getComments().get(0),CommentDto.class);
                int noOfComments = posts.get(i).getComments().size();

                dtos.get(i).setLastComment(commentDto);
                dtos.get(i).setNoOfComments(noOfComments);
            }

        }
        return PostsForumResult.builder().noOfPosts(noOfPosts).posts(dtos).build();
    }


    public Optional<Post> getPostByPostId(Integer postId){
        Optional<Post> postFind = postRepository.findById(postId);
        postFind.ifPresent(
                post -> {
                    post.setViews(post.getViews()+1);
                    postRepository.save(post);
                    post.getComments().sort((c1, c2) -> c2.getCreatedOn().compareTo(c1.getCreatedOn()));
                });
        return postFind;
    }

    public Post createNewPost (PostDetailDto p, UserEntity userEntity, Topic topic){
        // trick to avoid storing null in database, for query purposes
        if(p.getTags()!=null && p.getTags()!=""){
            p.setTags(p.getTags().toUpperCase(Locale.ROOT));
        }
        else{
            p.setTags("");
        }
        Post post = Post.builder()
                .title(p.getTitle())
                .status("ACCEPTED")
                .content(p.getContent())
                .createdOn(new Date())
                .author(userEntity)
                .topic(topic)
                .tags(p.getTags())
                .views(0)
                .build();

        return postRepository.save(post);
    }

    public Optional<Post> createNewComment (Post post, UserEntity userEntity, CommentDto c){
        Optional<Post> postFind = postRepository.findById(post.getPostId());
        if (postFind.isPresent()){
            Comment newComment = Comment.builder()
                    .commenter(userEntity)
                    .content(c.getContent())
                    .createdOn(new Date())
                    .enabled(c.getEnabled())
                    .build();
            post.addComment(newComment);
            Post savedPost = postRepository.save(post);
            savedPost.getComments().sort((c1, c2) -> c2.getCreatedOn().compareTo(c1.getCreatedOn()));
            return Optional.of(savedPost);
        }
        return Optional.empty();
    }

    public Optional<Post> editAPost (PostDetailDto p){
        Optional<Post> postFind = postRepository.findById(p.getPostId());
        Post post;
        if (postFind.isPresent()){
            post = postFind.get();
            post.setContent(p.getContent());
            post.setTags(p.getTags());
            post.setTitle(p.getTitle());
            post.setTopic(p.getTopic());
            post.setEditedOn(new Date());
            return Optional.of(postRepository.save(post));
        }
        return Optional.empty();
    }

    public List<PostViewDto>getHotPosts(int page){
        List<Post>posts = postRepository.getHotPosts(PageRequest.of(page-1,10));
        List<PostViewDto>viewPosts = MapperUtils.mapperList(posts,PostViewDto.class);
        for(int i = 0; i < posts.size(); i++){
            viewPosts.get(i).setNoOfComments(posts.get(i).getComments().size());
        }
        return viewPosts;
    }

    public int getNoOfCommentsByPostId(int postId){
        Optional<Post>post = postRepository.findById(postId);
        return post.isEmpty() ? 0 : post.get().getComments().size();
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

    public List<Post>getAllPostsByUserIdOrderByPending(String userId){
        return postRepository.getAllPostsByUserIdOrderByPending(userId);
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

    public List<PostViewDto> getAllPostsOrderByPending(){
        List<Post> posts = postRepository.getAllPostsOrderByPending();
        return MapperUtils.mapperList(posts,PostViewDto.class);
    }

    public List<PostViewDto> getAllPostsOrderByCreatedOn(){
        List<Post> posts = postRepository.findPostsByOrderByCreatedOnDesc();
        return MapperUtils.mapperList(posts,PostViewDto.class);
    }

    public void updatePostsStatus(List<Integer> postIds, String status) {
        postIds.forEach(id->{
            Optional<Post> p = postRepository.findById(id);
            p.ifPresent(
                    post -> {
                        post.setStatus(status);
                        postRepository.save(p.get());
                    });
        });
    }

    public boolean hasPending() {
        return postRepository.countByStatusEquals("PENDING") > 0;
    }

    public Integer getNoOfAllPosts(){
        return postRepository.findAll().size();
    }
}
