package com.seneca.senecaforum.repository;

import com.seneca.senecaforum.domain.Comment;
import com.seneca.senecaforum.domain.Post;
import com.seneca.senecaforum.domain.Topic;
import com.seneca.senecaforum.domain.User;
import com.seneca.senecaforum.utils.DatabaseUtils;
import com.seneca.senecaforum.utils.NumberStringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class CommentRepositoryTests {
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private TopicRepository topicRepository;



    @Test
    public void testCreateNewComments(){
        User randomUsr = DatabaseUtils.generateRandomObjFromUserDb(userRepository);
        Post randomPost = DatabaseUtils.generateRandomObjFromDb(postRepository,postRepository.findAll().iterator().next().getPostId());

        String content = NumberStringUtils.generateRandomString(15,false,true,true,true);
        Comment commentOne = new Comment().builder()
                .commenter(randomUsr)
                .content(content)
                .createdOn(new Date())
                .build();
        Comment commentTwo = new Comment().builder()
                .commenter(randomUsr)
                .content(content)
                .createdOn(new Date())
                .build();
        commentRepository.saveAll(List.of(commentOne,commentTwo));
        randomPost.addComment(commentOne);
        randomPost.addComment(commentTwo);
        postRepository.save(randomPost);
    }

    @Test
    public void testGetAllCommentsByPostId(){
        Post randomPost = DatabaseUtils.generateRandomObjFromDb(postRepository,postRepository.findAll().iterator().next().getPostId());
        List<Comment>comments = commentRepository.findAllByPostId(randomPost.getPostId(),PageRequest.of(0,10,Sort.by("created_on").descending())).getContent();
        assertThat(comments.size()>=0);
    }


    @Test
    public void testGetNoOfCommentsByTopicId(){
        Topic randomTopic = DatabaseUtils.generateRandomObjFromDb(topicRepository,topicRepository.findAll().iterator().next().getTopicId());
        int noOfCmts = commentRepository.getNoOfCommentsByTopicId(randomTopic.getTopicId());

        //confirm
        List<Post> posts = postRepository.findAll()
                .stream()
                .filter(p->p.getTopic().getTopicId()==randomTopic.getTopicId())
                .collect(Collectors.toList());

        Integer confirmSum = posts.stream()
                .map(p->p.getComments().size())
                .reduce(0,(a,b)-> a+b);
        assertThat(noOfCmts).isEqualTo(confirmSum);
    }


}
