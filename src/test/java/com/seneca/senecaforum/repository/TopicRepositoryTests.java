package com.seneca.senecaforum.repository;

import com.seneca.senecaforum.domain.Topic;
import com.seneca.senecaforum.utils.DatabaseUtils;
import com.seneca.senecaforum.utils.NumberStringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.anyOf;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class TopicRepositoryTests{
    @Autowired
    TopicRepository topicRepository;

    @Test
    public void testCreateOneTopic(){
        String name = "Assignment Help";
        Topic topic = Topic.builder().topicName(name).build();
        topicRepository.save(topic);
    }


    @Test
    public void testCreateNewTopics(){
        int before = topicRepository.findAll().size();
        List<String> names = List.of(
                "Assignment Help",
                "Exam Prep",
                "Side Projects",
                "Coop/ Internship",
                "Dating",
                "Coding Challenges",
                "Jobs");
        for(String n:names){
            Topic topic = Topic.builder().topicName(n).build();
            topicRepository.save(topic);
        }
        int after = topicRepository.findAll().size();
        assertThat(before).isEqualTo(after-names.size());
    }

    @Test
    public void testGetAllTopics(){
        List<Topic> topics = topicRepository.findAll();
        assertThat(topics.size()).isGreaterThan(0);
    }

}
