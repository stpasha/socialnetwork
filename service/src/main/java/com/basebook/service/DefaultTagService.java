package com.basebook.service;

import com.basebook.model.Tag;
import com.basebook.repository.TagRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DefaultTagService implements TagService{

    private final TagRepository tagRepository;

    public DefaultTagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Override
    public Tag get(Long id) {
        return tagRepository.findById(id);
    }

    @Override
    public List<Tag> getAll() {
        return tagRepository.findAll();
    }
    
    @Override
    public List<Tag> getTagsByPost(Long postId) {
        return tagRepository.findTagByPost(postId);
    }
}
