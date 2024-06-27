package com.utcn.service;

import com.utcn.model.Tag;
import com.utcn.repository.TagRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagService {
    private final TagRepository tagRepository;

    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public Tag createTagIfNotExists(String tagName) {
        Tag existingTag = tagRepository.findByName(tagName);
        if(existingTag == null) {
            Tag newTag = new Tag(tagName);
            return tagRepository.save(newTag);
        }
        return existingTag;
    }

    public List<Tag> getAllTags() {
        return tagRepository.findAll();
    }
}
