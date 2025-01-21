package com.basebook.service;

import com.basebook.model.Tag;

import java.util.List;

public interface TagService {

    Tag get(Long id);

    List<Tag> getAll();

    List<Tag> getTagsByPost(Long postId);

}
