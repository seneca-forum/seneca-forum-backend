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
    public void testCreateNewTopics(){
        int before = topicRepository.findAll().size();
        List<String> names = List.of(
                "Assignment Help",
                "Exam Prep",
                "Side Projects",
                "Coop/Internship",
                "Volunteer",
                "Coding Challenges",
                "Jobs");
        for(String n:names){
            Topic topic = Topic.builder().topicName(n).views(0).build();
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


    @Test
    public void testUpdateViewByTopicId(){
        Topic randomTopic = DatabaseUtils.generateRandomObjFromDb(topicRepository,topicRepository.findAll().iterator().next().getTopicId());

        int views = NumberStringUtils.generateRandomNumber(1,100);
        randomTopic.setViews(views);
        topicRepository.save(randomTopic);

        Topic afterUpdate = topicRepository.findById(randomTopic.getTopicId()).get();
        assertThat(afterUpdate.getViews()).isEqualTo(views);
    }

    @Test
    public void testGetAllTopicsInDescendingViews(){
        List<Topic> topics = topicRepository.findAll();
        for(int i = 1; i < topics.size(); i++){
            int value = topics.get(i).compareTo(topics.get(i-1));
            assertTrue(value==0||value==1);
        }
    }


}
