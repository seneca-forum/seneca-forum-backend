package com.seneca.senecaforum.service;

import com.seneca.senecaforum.domain.Post;
import com.seneca.senecaforum.domain.Topic;
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
                    topic, methodOrder,tags,PageRequest.of(page - 1, 10));
        }
        if (posts.size() == 0){
            return null;
        }
            List<PostDto> postPage = MapperUtils.mapperList(posts, PostDto.class);
            String a = "b";
            for (int i = 0; i < posts.size(); ++i) {
                int noOfComments = posts.get(i).getComments().size();
                if (noOfComments == 0) continue;
                if(Objects.isNull(methodOrder) || methodOrder.equals("desc")){
                    postPage.get(i).setLastComment(
                            MapperUtils.mapperObject(posts.get(i).getComments().get(noOfComments-1), CommentDto.class));
                }else{
                    postPage.get(i).setLastComment(
                            MapperUtils.mapperObject(posts.get(i).getComments().get(0), CommentDto.class));
                }
                postPage.get(i).setNoOfComments(noOfComments);
            }
            return postPage;

        }

}
