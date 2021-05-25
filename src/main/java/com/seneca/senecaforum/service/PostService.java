package com.seneca.senecaforum.service;

import com.seneca.senecaforum.client.exception.BadRequestException;
import com.seneca.senecaforum.domain.Comment;
import com.seneca.senecaforum.domain.Post;
import com.seneca.senecaforum.domain.Topic;
import com.seneca.senecaforum.domain.User;
import com.seneca.senecaforum.repository.PostRepository;
import com.seneca.senecaforum.service.dto.CommentDto;
import com.seneca.senecaforum.service.dto.PostDto;
import com.seneca.senecaforum.service.utils.ApplicationUtils;
import com.seneca.senecaforum.service.utils.MapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    public List<PostDto> getAllPostByTopic(
            Topic topic,String methodOrder,String start,String end,int page,String sortBy,String tags
    ) throws ParseException {
        List<Post> posts = null;
        boolean checkOder = Objects.nonNull(methodOrder);
        if (Objects.nonNull(sortBy)) {
            if (Objects.isNull(start) && Objects.isNull(end)) {
                if (sortBy.equals("posts")) {// sort by post and order by lastest or oldest created on post
                    if (checkOder && methodOrder.equals("asc"))
                        posts = postRepository.findPostsByTopic(
                                topic, PageRequest.of(page - 1, 10, Sort.by(Sort.Direction.ASC, "createdOn")));
                    else
                        posts = postRepository.findPostsByTopic(
                                topic, PageRequest.of(page - 1, 10, Sort.by(Sort.Direction.DESC, "createdOn")));
                } else if (sortBy.equals("tags")) {// sort by tags and order by lastest or oldest created on post
                    if (checkOder && methodOrder.equals("asc"))
                        posts = postRepository.findByFilterDateAndTags(
                                topic, tags, null, null,
                                PageRequest.of(page - 1, 10, Sort.by(Sort.Direction.ASC, "createdOn")));
                    else
                        posts = postRepository.findByFilterDateAndTags(
                                topic, tags, null, null,
                                PageRequest.of(page - 1, 10, Sort.by(Sort.Direction.DESC, "createdOn")));
                }
            }else {
                //sort by start and end date including tags or not
//                posts = postRepository.findByFilterDateAndTags(
//                        topic,tags, ApplicationUtils.convertToDate(start),ApplicationUtils.convertToDate(end),
//                        PageRequest.of(page-1,10,Sort.by(Sort.Direction.DESC,"createdOn")));
            }
            } else { //sort by comment created on oldest
                String a = "b";
                posts = postRepository.findPostsByTopicDefault(
                        topic,methodOrder,null,null,tags,PageRequest.of(page-1,10));
            }
//        if (posts.size() == 0){
//            return null;
//        }
        List<PostDto> postPage = MapperUtils.mapperList(posts, PostDto.class);
        String a = "b";
        for (int i = 0; i < posts.size(); ++i) {
            int noOfComments = posts.get(i).getComments().size();
            if (noOfComments == 0) continue;
            postPage.get(i).setLastComment(
                    MapperUtils.mapperObject(posts.get(i).getComments().get(noOfComments - 1), CommentDto.class));
            postPage.get(i).setNoOfComments(noOfComments);
        }
        return postPage;
    }

    public Post getPostByPostId(Integer postId){
        Post post = null;
        try{
            post = postRepository.findById(postId).get();
        }
        catch(Exception ex){
            throw new BadRequestException("Found nothing with postId "+postId);
        }
        Collections.sort(post.getComments(),(c1, c2)->c2.getCreatedOn().compareTo(c1.getCreatedOn()));
        return post;
    }

    public Post createNewPost(PostDto p, User user, Topic topic){
        Post post = new Post().builder()
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

    public Post createNewComment(Post post, User user, CommentDto c){
        Comment newComment = new Comment().builder()
                .commenter(user)
                .content(c.getContent())
                .createdOn(new Date())
                .enabled(c.getEnabled())
                .build();
        post.addComment(newComment);
        Post savedPost = postRepository.save(post);
        Collections.sort(savedPost.getComments(),(c1,c2)->c2.getCreatedOn().compareTo(c1.getCreatedOn()));
        return post;
    }

    public Post editAPost(PostDto p){
        Post savedPost = postRepository.findById(p.getPostId()).get();
        savedPost.setContent(p.getContent());
        savedPost.setTags(p.getTags());
        savedPost.setTitle(p.getTitle());
        savedPost.setTopic(p.getTopic());
        return postRepository.save(savedPost);
    }


}
