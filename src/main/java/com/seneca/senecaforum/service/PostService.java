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

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private TopicRepository topicRepository;

    public List<PostDto> getAllPostByTopic(
            Topic topic,String orderBy,String start,String end,int page,String sortBy
    ){
        Page<Post> posts = null;
        if (Objects.nonNull(sortBy)){
            if (sortBy.equals("comment")){
                if (orderBy.equals("asc"))
                    posts = postRepository.findAllByTopic(
                            topic,PageRequest.of(page-1,10,Sort.by(Sort.Direction.ASC,"createdOn")));
                else if (orderBy.equals("desc"))
                    posts = postRepository.findAllByTopic(
                            topic,PageRequest.of(page-1,10,Sort.by(Sort.Direction.DESC,"createdOn")));
            }
//            else {
//                posts = postRepository.findAllByTopicAndTagsContainsAndCreatedOnBetween(
//                        topic,sortBy.
//                )
//            }
        }else{
            if (Objects.nonNull(orderBy) && orderBy.equals("asc")){
                posts = postRepository.findAllByTopicOrderByCommentsCreatedOnAsc(
                        topic, PageRequest.of(page-1, 10)
                );
            }else {
                posts = postRepository.findAllByTopicOrderByCommentsCreatedOnDesc(
                        topic, PageRequest.of(page-1,10));
            }
        }
//        ModelMapper modelMapper = new ModelMapper();
//        List<PostDto> postDtos = modelMapper.map(posts,new TypeToken<List<PostDto>>(){}.getType());
//        String a = "b";
        List<PostDto> a = MapperUtils.getInstance().mapperList(posts.getContent(),PostDto.class);
        return MapperUtils.getInstance().mapperList(posts.getContent(),PostDto.class);
    }




}
