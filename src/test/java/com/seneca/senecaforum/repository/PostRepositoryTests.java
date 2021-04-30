package com.seneca.senecaforum.repository;

import com.seneca.senecaforum.domain.Post;
import com.seneca.senecaforum.domain.Topic;
import com.seneca.senecaforum.domain.User;
import com.seneca.senecaforum.utils.DatabaseUtils;
import com.seneca.senecaforum.utils.NumberStringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;



@SpringBootTest
public class PostRepositoryTests {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private PostRepository postRepository;

    @Test
    public void addNewPostWithTags(){
        int before = postRepository.findAll().size();
        User randomUsr = DatabaseUtils.generateRandomObjFromDb(userRepository,userRepository.findAll().iterator().next().getUserId());
        Topic randomTopic = DatabaseUtils.generateRandomObjFromDb(topicRepository,topicRepository.findAll().iterator().next().getTopicId());
        String title = NumberStringUtils.generateRandomString(15,false,true,true,true);
        String tags= "";
        for(int i = 0; i < 5; i++){
            String t = "#"+NumberStringUtils.generateRandomString(5,false,false,true,false);
            tags+=t;
        }

        Post post = new Post().builder()
                .title(title)
                .createdOn(new Date())
                .user(randomUsr)
                .topic(randomTopic)
                .tags(tags)
                .build();
        postRepository.save(post);

        int after = postRepository.findAll().size();
        assertThat(before).isEqualTo(after-1);
    }

    @Test
    public void addNewPostWithNoTags(){
        int before = postRepository.findAll().size();
        User randomUsr = DatabaseUtils.generateRandomObjFromDb(userRepository,userRepository.findAll().iterator().next().getUserId());
        Topic randomTopic = DatabaseUtils.generateRandomObjFromDb(topicRepository,topicRepository.findAll().iterator().next().getTopicId());
        String title = NumberStringUtils.generateRandomString(15,false,true,true,true);

        Post post = new Post().builder()
                .title(title)
                .createdOn(new Date())
                .user(randomUsr)
                .topic(randomTopic)
                .build();
        postRepository.save(post);

        int after = postRepository.findAll().size();
        assertThat(before).isEqualTo(after-1);
    }


}

