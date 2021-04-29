package com.seneca.senecaforum;

import com.seneca.senecaforum.domain.Post;
import com.seneca.senecaforum.domain.Topic;
import com.seneca.senecaforum.repository.PostRepository;
import com.seneca.senecaforum.repository.TopicRepository;
import com.seneca.senecaforum.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class SenecaForumApplicationTests {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private TopicRepository topicRepository;

    @Test
    public void addNewPost(){
        Post post = new Post().builder()
                .title("Java")
                .createdOn(new Date())
                .user(userRepository.findByUserId("AT14").get())
                .topic(topicRepository.findById(2).get())
                .build();
        postRepository.save(post);
    }

    @Test
    public void addNewTopic(){
        Topic topic = Topic.builder().topicName("Exam Prep").build();
        topicRepository.save(topic);
    }

    @Test
    public void getAllTopic(){
        List<Topic> topics = topicRepository.findAll();
        String a = "b";
    }

    @Test
    public void getAllPostByTopicId(){
        List<Post> posts = postRepository.findAllByTopicId(1);
        posts.forEach(post -> System.out.println(post.getTitle()));
        String a = "b";
    }
}
