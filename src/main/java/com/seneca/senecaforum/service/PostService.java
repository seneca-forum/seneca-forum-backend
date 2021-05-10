package com.seneca.senecaforum.service;

import com.seneca.senecaforum.domain.Post;
import com.seneca.senecaforum.domain.Topic;
import com.seneca.senecaforum.repository.PostRepository;
import com.seneca.senecaforum.repository.TopicRepository;
import com.seneca.senecaforum.service.dto.PostDto;
import com.seneca.senecaforum.service.utils.MapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private TopicRepository topicRepository;

    public List<PostDto> getAllPostByTopic(
            Topic topic,String orderBy,String start,String end,int page,String sortBy,String tags
    ){
        Page<Post> posts = null;
        // sort by not null
        if (Objects.nonNull(sortBy) && Objects.isNull(start) && Objects.isNull(end)){
            if (sortBy.equals("posts")){// sort by post and order by lastest or oldest created on post
                if (orderBy.equals("asc"))
                    posts = postRepository.findDistinctByTopic(
                            topic,PageRequest.of(page-1,10,Sort.by(Sort.Direction.ASC,"createdOn")));
                else
                    posts = postRepository.findDistinctByTopic(
                            topic,PageRequest.of(page-1,10,Sort.by(Sort.Direction.DESC,"createdOn")));
            }
            else if (sortBy.equals("tags")){// sort by tags and order by lastest or oldest created on post
                if (orderBy.equals("asc"))
                    posts = postRepository.findDistinctByTopicAndTagsContains(
                            topic,tags,PageRequest.of(page-1,10,Sort.by(Sort.Direction.ASC,"createdOn")));
                else
                    posts = postRepository.findDistinctByTopicAndTagsContains(
                            topic,tags,PageRequest.of(page-1,10,Sort.by(Sort.Direction.DESC,"createdOn")));
            }
        }else{ //sort by comment created on oldest
            if (Objects.nonNull(orderBy) && orderBy.equals("asc")){
                posts = postRepository.findDistinctByTopic(
                        topic,
                        PageRequest.of(page-1,10,Sort.by(Sort.Direction.ASC,"comments.createdOn"))
                );
            }else { //sort by comment created on lastest(default)
                posts = postRepository.findDistinctByTopic(
                        topic,
                        PageRequest.of(page-1,10,Sort.by(Sort.Direction.DESC,"comments.createdOn"))
                );
            }
        }
        return MapperUtils.mapperList(posts.getContent(),PostDto.class);
    }




}
