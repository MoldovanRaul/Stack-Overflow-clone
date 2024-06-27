package com.utcn.controller;

import com.utcn.dto.TagDTO;
import com.utcn.model.Tag;
import com.utcn.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tags")
public class TagController {
    @Autowired
    private final TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping
    public List<TagDTO> getAllTags() {
        List<Tag> tags = tagService.getAllTags();
        return  tags.stream().map(TagDTO::new).toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TagDTO createTag(@RequestBody Tag tag) {
        return new TagDTO(tagService.createTagIfNotExists(tag.getName()));
    }
}
