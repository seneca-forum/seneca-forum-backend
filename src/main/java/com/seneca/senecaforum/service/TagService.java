package com.seneca.senecaforum.service;

import com.seneca.senecaforum.domain.Post;
import com.seneca.senecaforum.domain.Tag;
import com.seneca.senecaforum.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TagService {
    @Autowired
    TagRepository tagRepository;

    public void createTag(Post savedPost){
        List<Tag>tags = tagRepository.findAll();
        List<String>tagsDb = tags.stream().map(Tag::getTagName).collect(Collectors.toList());
        List<String>tagsUsr = Arrays.stream(savedPost.getTags().toLowerCase().split("#")).collect(Collectors.toList());
        tagsUsr.removeAll(tagsDb);
        tagsUsr.remove(tagsUsr.indexOf(""));
        tagsUsr.forEach(t->{
            Tag newTag = new Tag().builder().tagName(t).build();
            tagRepository.save(newTag);
        });
    }
}
