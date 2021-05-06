package com.seneca.senecaforum.repository;

import com.seneca.senecaforum.domain.Post;
import com.seneca.senecaforum.domain.Topic;
import com.seneca.senecaforum.domain.User;
import com.seneca.senecaforum.utils.DatabaseUtils;
import com.seneca.senecaforum.utils.NumberStringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import javax.xml.crypto.Data;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

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
        String content = NumberStringUtils.generateRandomString(25,false,true,true,true);
        String tags= "";
        for(int i = 0; i < 5; i++){
            String t = "#"+NumberStringUtils.generateRandomString(5,false,false,true,false);
            tags+=t;
        }

        Post post = new Post().builder()
                .title(title)
                .content(content)
                .createdOn(new Date())
                .author(randomUsr)
                .topic(randomTopic)
                .tags(tags)
                .views(0)
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
        String title = NumberStringUtils.generateRandomString(35,false,true,true,true);
        String content = NumberStringUtils.generateRandomString(25,false,true,true,true);

        Post post = new Post().builder()
                .title(title)
                .content(content)
                .createdOn(new Date())
                .author(randomUsr)
                .topic(randomTopic)
                .views(0)
                .build();
        postRepository.save(post);

        int after = postRepository.findAll().size();
        assertThat(before).isEqualTo(after-1);
    }

    @Test
    public void testAddNewPostsWithTagsFromEachTopic(){
        int before = postRepository.findAll().size();
        int topicSize = topicRepository.findAll().size();
        for(int i = 1; i <= topicSize; i++){
            User randomUsr = DatabaseUtils.generateRandomObjFromDb(userRepository,userRepository.findAll().iterator().next().getUserId());
            Topic randomTopic = topicRepository.findById(i).get();
            String title = NumberStringUtils.generateRandomString(45,false,true,true,true);
            String content = NumberStringUtils.generateRandomString(25,false,true,true,true);

            String tags= "";
            for(int j = 0; j < 5; j++){
                String t = "#"+NumberStringUtils.generateRandomString(5,false,false,true,false);
                tags+=t;
            }

            Post post = new Post().builder()
                    .title(title)
                    .content(content)
                    .createdOn(new Date())
                    .author(randomUsr)
                    .topic(randomTopic)
                    .tags(tags)
                    .views(0)
                    .build();
            postRepository.save(post);

        }

        int after = postRepository.findAll().size();
        assertThat(before).isEqualTo(after-topicSize);
    }
    @Test
    public void testGetAllPostsFromTopicId(){
        Post randomPost = DatabaseUtils.generateRandomObjFromDb(postRepository,postRepository.findAll().iterator().next().getPostId());

        int topicId = randomPost.getTopic().getTopicId();
        List<Post>posts = postRepository.findAllByTopicId(topicId);
        for(Post p:posts){
            assertThat(p.getTopic().getTopicId()).isEqualTo(topicId);
        }
    }


    @Test
    public void testDeleteAPost(){
        Post randomPost = DatabaseUtils.generateRandomObjFromDb(postRepository,postRepository.findAll().iterator().next().getPostId());
        postRepository.delete(randomPost);
        Optional<Post>deletedPost = postRepository.findById(randomPost.getPostId());
        assertThat(deletedPost).isEmpty();
    }

    @Test
    public void testUpdatePostContent(){
        Post randomPost = DatabaseUtils.generateRandomObjFromDb(postRepository,postRepository.findAll().iterator().next().getPostId());
        String newContent = NumberStringUtils.generateRandomString(30,false,true,true,true);
        randomPost.setContent(newContent);
        Post updatedPost = postRepository.save(randomPost);
        assertThat(updatedPost.getContent()).isEqualTo(newContent);
    }

    @Test
    public void testUpdateAPostViews(){
        Post randomPost = DatabaseUtils.generateRandomObjFromDb(postRepository,postRepository.findAll().iterator().next().getPostId());
        Integer currentViews = randomPost.getViews();
        Integer newViews = NumberStringUtils.generateRandomNumber(0,20)+currentViews;
        randomPost.setViews(newViews);
        postRepository.save(randomPost);
        assertThat(randomPost.getViews()).isEqualTo(newViews);
    }

    @Test
    public void testFilterPosts() {
        String tag = "test";
        java.sql.Date startDate = java.sql.Date.valueOf("2020-05-05");
        java.sql.Date endDate = java.sql.Date.valueOf("2021-05-05");
        List<Post> posts = postRepository.filterPosts(1,tag, startDate,endDate);

        for(Post p:posts){
            assertThat(p.getTopic().getTopicId()).isEqualTo(1);

            assertThat(p.getCreatedOn()).isAfterOrEqualTo(startDate).isBeforeOrEqualTo(endDate);
        }

    }

}

