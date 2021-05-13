package com.seneca.senecaforum.service;

import com.seneca.senecaforum.domain.Post;
import com.seneca.senecaforum.domain.Topic;
import com.seneca.senecaforum.repository.PostRepository;
import com.seneca.senecaforum.repository.TopicRepository;
import com.seneca.senecaforum.service.dto.CommentDto;
import com.seneca.senecaforum.service.dto.PostDto;
import com.seneca.senecaforum.service.utils.ApplicationUtils;
import com.seneca.senecaforum.service.utils.MapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.List;
import java.util.Objects;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private TopicRepository topicRepository;

    public List<PostDto> getAllPostByTopic(
            Topic topic,String orderBy,String start,String end,int page,String sortBy,String tags
    ) throws ParseException {
        Page<Post> posts = null;
        boolean checkOder = Objects.nonNull(orderBy);
        if (Objects.nonNull(sortBy)){
            if (Objects.isNull(start) && Objects.isNull(end)){
                if (sortBy.equals("posts")){// sort by post and order by lastest or oldest created on post
                    if (checkOder && orderBy.equals("asc"))
                        posts = postRepository.findDistinctByTopic(
                                topic,PageRequest.of(page-1,10,Sort.by(Sort.Direction.ASC,"createdOn")));
                    else
                        posts = postRepository.findDistinctByTopic(
                                topic,PageRequest.of(page-1,10,Sort.by(Sort.Direction.DESC,"createdOn")));
                }
                else if (sortBy.equals("tags")){// sort by tags and order by lastest or oldest created on post
                    if (checkOder && orderBy.equals("asc"))
                        posts = postRepository.findByFilterDateAndTags(
                                topic,tags, null,null,
                                PageRequest.of(page-1,10,Sort.by(Sort.Direction.ASC,"createdOn")));
                    else
                        posts = postRepository.findByFilterDateAndTags(
                                topic,tags, null,null,
                                PageRequest.of(page-1,10,Sort.by(Sort.Direction.DESC,"createdOn")));
                }
            }else {
                //sort by start and end date including tags or not
                posts = postRepository.findByFilterDateAndTags(
                        topic,tags, ApplicationUtils.convertToDate(start),ApplicationUtils.convertToDate(end),
                        PageRequest.of(page-1,10,Sort.by(Sort.Direction.DESC,"createdOn")));
            }
        }
        else{ //sort by comment created on oldest
            if (checkOder && orderBy.equals("asc")){
                posts = postRepository.findDistinctByTopic(topic,
                        PageRequest.of(page-1,10,Sort.by(Sort.Direction.ASC,"comments.createdOn"))
                );
            }else { //sort by comment created on lastest(DEFAULT)
                posts = postRepository.findDistinctByTopic(topic,
                        PageRequest.of(page-1,10,Sort.by(Sort.Direction.DESC,"comments.createdOn"))
                );
            }
        }
        if (posts.getSize() == 0){
            return null;
        }
        List<PostDto> postPage = MapperUtils.mapperList(posts.getContent(),PostDto.class);
        for (int i = 0;i < posts.getTotalElements();++i){
            if (posts.getContent().get(i).getComments().size() == 0) continue;
            postPage.get(i).setLastComment(
                    MapperUtils.mapperObject(posts.getContent().get(i).getComments().get(0), CommentDto.class));
            postPage.get(i).setNoOfComments(posts.getContent().get(i).getComments().size());
        }
        return postPage;
    }

}
