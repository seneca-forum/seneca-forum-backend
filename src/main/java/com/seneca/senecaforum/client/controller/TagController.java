package com.seneca.senecaforum.client.controller;

import com.seneca.senecaforum.domain.Tag;
import com.seneca.senecaforum.domain.Topic;
import com.seneca.senecaforum.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@RestController
@RequestMapping("/api/tags")
public class TagController {
    @Autowired
    private TagRepository tagRepository;

    @GetMapping
    public ResponseEntity<Set<Tag>> getAllPostsByPostID(){
        List<Tag> tags = tagRepository.findAll();
        Set<Tag>tagSet = new TreeSet<>();
        for(Tag t:tags){
            tagSet.add(t);
        }
        return ResponseEntity.ok(tagSet);
    }
}
