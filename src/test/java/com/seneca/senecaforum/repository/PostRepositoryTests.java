package com.seneca.senecaforum.repository;

import com.seneca.senecaforum.domain.Post;
import com.seneca.senecaforum.domain.Topic;
import com.seneca.senecaforum.domain.User;
import com.seneca.senecaforum.service.PostService;
import com.seneca.senecaforum.service.dto.PostDto;
import com.seneca.senecaforum.service.utils.ApplicationUtils;
import com.seneca.senecaforum.utils.DatabaseUtils;
import com.seneca.senecaforum.utils.NumberStringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;


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
        User randomUsr = DatabaseUtils.generateRandomObjFromUserDb(userRepository);
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
        User randomUsr = DatabaseUtils.generateRandomObjFromUserDb(userRepository);
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
            User randomUsr = DatabaseUtils.generateRandomObjFromUserDb(userRepository);
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
//    @Test
//    public void testGetAllPostsFromTopicId(){
//        Post randomPost = DatabaseUtils.generateRandomObjFromDb(postRepository,postRepository.findAll().iterator().next().getPostId());
//
//        int topicId = randomPost.getTopic().getTopicId();
//        Optional<Topic> topic = topicRepository.findById(1);
//        Page<Post> posts = postRepository.findByTopicOrderByCommentsCreatedOnDesc(topic.get(),PageRequest.of(
//                0,10)
//        );
//        String a = "b";
//        posts.forEach(post -> System.out.println(post.getTopic()));
//    }

//    @Test
//    public void testAPIGetAllPostsFromTopic(){
//        Optional<Topic> topic = topicRepository.findById(1);
//        List<PostDto> test = postService.getAllPostByTopic(topic.get(),,)
//    }


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

    @Autowired
    private PostService postService;

    @Test
    public void testCustomFilterPost() throws ParseException {
        Topic topic = topicRepository.findById(4).get();
//        List<Post> posts= postRepository.findByFilterDateAndTags(topic,null, ApplicationUtils.convertToDate("2021-05-06"),new Date());
        String a = "b";
    }

}

