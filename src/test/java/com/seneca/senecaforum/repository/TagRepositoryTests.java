package com.seneca.senecaforum.repository;

import com.seneca.senecaforum.domain.Post;
import com.seneca.senecaforum.domain.Tag;
import com.seneca.senecaforum.utils.DatabaseUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;
import java.util.stream.Collectors;

@SpringBootTest
public class TagRepositoryTests {
    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private PostRepository postRepository;

    @Test
    public void testCreateTags(){
        Post randomPost = DatabaseUtils.generateRandomObjFromDb(postRepository,postRepository.findAll().iterator().next().getPostId());

        // extract tag string
        String tags = randomPost.getTags();
        List<String> tokens = new ArrayList<>();
        String[] newStr = tags.toLowerCase().split("#");
        for(String word:newStr){
            String regex = "[^A-za-z0-9]";
            String after = word.replaceAll(regex,"");
            if(after!=""){
                tokens.add(after.trim());
            }
        }

        List<Tag> tagsDB = tagRepository.findAll();
        List<String> tagString = tagsDB.stream().map(Tag::getTagName).collect(Collectors.toList());
        for(String t:tokens){
            if(!tagString.contains(t)){
                Tag newTag = new Tag(t);
                tagRepository.save(newTag);
                tagsDB.add(newTag);
            }
        }
    }
}
