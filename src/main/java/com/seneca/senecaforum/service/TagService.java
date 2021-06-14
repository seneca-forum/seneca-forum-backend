package com.seneca.senecaforum.service;

import com.seneca.senecaforum.domain.Tag;
import com.seneca.senecaforum.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TagService {
    @Autowired
    TagRepository tagRepository;

    public void createTag(String tag){
        Set<String> tags = tagRepository.findAll().stream().map(Tag::getTagName).collect(Collectors.toSet());
        String[] tagInput = tag.toLowerCase().split("#");
        for (String tagName:tagInput){
            if (!tags.contains(tagName)) {
                Tag newTag = new Tag();
                newTag.setTagName(tagName);
                tagRepository.save(newTag);
            }
        }
    }
}
