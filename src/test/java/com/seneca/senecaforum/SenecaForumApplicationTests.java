package com.seneca.senecaforum;

import com.seneca.senecaforum.domain.Post;
import com.seneca.senecaforum.domain.Topic;
import com.seneca.senecaforum.repository.PostRepository;
import com.seneca.senecaforum.repository.TopicRepository;
import com.seneca.senecaforum.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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
        Topic topicExam = Topic.builder().topicName("Exam Prep").build();
        Topic topicSideProjects = Topic.builder().topicName("Side Projects").build();
        Topic topicIntern = Topic.builder().topicName("Coop/Internship").build();
        Topic topicVolun = Topic.builder().topicName("Volunteer").build();
        Topic topicProgramming = Topic.builder().topicName("Coding Challenges").build();
        Topic topicJobs = Topic.builder().topicName("Jobs").build();
        Topic topicDating = Topic.builder().topicName("Dating").build();
        topicRepository.saveAll(List.of(topicExam,topicDating,topicJobs,topicIntern,topicVolun,topicProgramming,topicSideProjects));
    }

    @Test
    public void getAllTopic(){
        List<Topic> topics = topicRepository.findAll();
        String a = "b";
    }

    @Test
    public void getAllPostByTopicId(){
        Pageable pagination = PageRequest.of(0,10,Sort.by("noOfReplies").descending());
        List<Post> pots = postRepository.findAllByTopicId(1,pagination);
        pots.forEach(post -> System.out.println(post.getTitle()));
        String a = "b";
    }


}
