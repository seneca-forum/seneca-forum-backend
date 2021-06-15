package com.seneca.senecaforum.service;

import com.seneca.senecaforum.client.exception.BadRequestException;
import com.seneca.senecaforum.domain.Comment;
import com.seneca.senecaforum.domain.Post;
import com.seneca.senecaforum.domain.Topic;
import com.seneca.senecaforum.domain.UserEntity;
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
        if(p.getTags()==null){
            p.setTags("");
        }
        Post post = Post.builder()
                .title(p.getTitle())
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
            return Optional.of(postRepository.save(post));
        }
        return Optional.empty();
    }

    public List<PostViewDto>getHotPosts(int page){
        List<Post>posts = postRepository.getHotPosts(PageRequest.of(page-1,10));
        List<PostViewDto>viewPosts = MapperUtils.mapperList(posts,PostViewDto.class);
        viewPosts.forEach(p->p.setNoOfComments(getNoOfCommentsByPostId(p.getPostId())));
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
        return postRepository.countByStatusEquals("pending") > 0;
    }

    public Integer getNoOfAllPosts(){
        return postRepository.findAll().size();
    }
}
