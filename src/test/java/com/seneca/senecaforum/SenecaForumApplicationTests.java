package com.seneca.senecaforum;

import com.seneca.senecaforum.domain.Post;
import com.seneca.senecaforum.domain.User;
import com.seneca.senecaforum.repository.PostRepository;
import com.seneca.senecaforum.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;

@SpringBootTest
public class SenecaForumApplicationTests {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Test
    public void addNewPost(){
        Post post = new Post().builder()
                .title("Can gap tai lieu toiec")
                .createdOn(new Date())
                .user(userRepository.findByUserId("AT14").get())
                .build();
        postRepository.save(post);
    }

    @Test
    public void getAllPost(){
        List<Post> posts = postRepository.findAll();
        String a = "1";
    }

}
