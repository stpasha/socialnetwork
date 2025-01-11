package com.basebook.service;

import com.basebook.model.Tag;

import java.util.List;

public interface TagService {

    Tag get(Long id);

    void create(Tag tag);

    void delete(Long id);

    List<Tag> getTagsByPost(Long postId);

}
